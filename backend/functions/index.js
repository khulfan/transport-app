const functions = require('firebase-functions');
const admin = require('firebase-admin');
const axios = require('axios');

admin.initializeApp();

/**
 * Cloud Function triggered when a stop status changes to "ARRIVED"
 * Automatically sends WhatsApp messages to all passengers on that route
 */
exports.sendStopArrivalNotification = functions.firestore
    .document('routes/{routeId}')
    .onUpdate(async (change, context) => {
        const routeId = context.params.routeId;
        const beforeData = change.before.data();
        const afterData = change.after.data();

        // Check which stop changed to ARRIVED
        const beforeStops = beforeData.stops || [];
        const afterStops = afterData.stops || [];

        for (let i = 0; i < afterStops.length; i++) {
            const beforeStop = beforeStops[i];
            const afterStop = afterStops[i];

            // Check if this stop just changed to ARRIVED
            if (beforeStop && afterStop &&
                beforeStop.status !== 'ARRIVED' &&
                afterStop.status === 'ARRIVED') {

                console.log(`Stop ${i} arrived: ${afterStop.stopName}`);

                // Send WhatsApp notifications
                await sendWhatsAppNotifications(
                    routeId,
                    afterData.routeName,
                    afterStop.stopName,
                    i
                );
            }
        }

        return null;
    });

/**
 * Send WhatsApp messages to all passengers on the route
 */
async function sendWhatsAppNotifications(routeId, routeName, stopName, stopIndex) {
    try {
        // Get all passengers subscribed to this route
        const usersSnapshot = await admin.firestore()
            .collection('users')
            .where('routeId', '==', routeId)
            .where('role', '==', 'PASSENGER')
            .get();

        if (usersSnapshot.empty) {
            console.log('No passengers found for route:', routeId);
            return;
        }

        // Get WhatsApp credentials from environment
        const whatsappToken = functions.config().whatsapp?.token;
        const whatsappPhoneId = functions.config().whatsapp?.phone;

        if (!whatsappToken || !whatsappPhoneId) {
            console.error('WhatsApp credentials not configured');
            return;
        }

        // Send message to each passenger
        const promises = [];
        usersSnapshot.forEach(doc => {
            const user = doc.data();
            const phone = formatPhoneNumber(user.phone);

            if (phone) {
                promises.push(
                    sendWhatsAppMessage(
                        whatsappToken,
                        whatsappPhoneId,
                        phone,
                        routeName,
                        stopName
                    )
                );
            }
        });

        await Promise.all(promises);
        console.log(`Sent ${promises.length} WhatsApp notifications`);

    } catch (error) {
        console.error('Error sending WhatsApp notifications:', error);
    }
}

/**
 * Send individual WhatsApp message using Cloud API
 */
async function sendWhatsAppMessage(token, phoneNumberId, recipientPhone, routeName, stopName) {
    try {
        const url = `https://graph.facebook.com/v18.0/${phoneNumberId}/messages`;

        const message = `🚌 *Bus Alert*\n\nThe bus on route *${routeName}* has arrived at *${stopName}*.\n\nPlease be ready!`;

        const data = {
            messaging_product: 'whatsapp',
            to: recipientPhone,
            type: 'text',
            text: {
                body: message
            }
        };

        const config = {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        };

        const response = await axios.post(url, data, config);
        console.log(`WhatsApp sent to ${recipientPhone}:`, response.data);

        return response.data;

    } catch (error) {
        console.error(`Error sending WhatsApp to ${recipientPhone}:`, error.response?.data || error.message);
        throw error;
    }
}

/**
 * Format phone number to WhatsApp format (without + or spaces)
 * Example: +1234567890 -> 1234567890
 */
function formatPhoneNumber(phone) {
    if (!phone) return null;
    return phone.replace(/\D/g, ''); // Remove all non-digit characters
}

/**
 * Test function to send a WhatsApp message manually
 * Can be called via HTTP for testing
 */
exports.testWhatsApp = functions.https.onRequest(async (req, res) => {
    try {
        const { phone, routeName, stopName } = req.body;

        if (!phone || !routeName || !stopName) {
            res.status(400).send('Missing parameters');
            return;
        }

        const whatsappToken = functions.config().whatsapp?.token;
        const whatsappPhoneId = functions.config().whatsapp?.phone;

        if (!whatsappToken || !whatsappPhoneId) {
            res.status(500).send('WhatsApp not configured');
            return;
        }

        const formattedPhone = formatPhoneNumber(phone);
        await sendWhatsAppMessage(
            whatsappToken,
            whatsappPhoneId,
            formattedPhone,
            routeName,
            stopName
        );

        res.status(200).send('WhatsApp message sent successfully');

    } catch (error) {
        console.error('Test WhatsApp error:', error);
        res.status(500).send('Error sending message');
    }
});
