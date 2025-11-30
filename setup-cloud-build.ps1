# Quick Start Script for Cloud Build Setup
# Run this script to prepare your project for GitHub Actions cloud build

Write-Host "Transport App - Cloud Build Setup" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Check Git
Write-Host "Step 1: Checking Git installation..." -ForegroundColor Yellow
try {
    $gitVersion = git --version
    Write-Host "Git is installed: $gitVersion" -ForegroundColor Green
} catch {
    Write-Host "Git is not installed!" -ForegroundColor Red
    Write-Host "Please install Git from: https://git-scm.com/download/win" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Step 2: Initialize Git Repository
Write-Host "Step 2: Initializing Git repository..." -ForegroundColor Yellow
if (Test-Path ".git") {
    Write-Host "Git repository already initialized" -ForegroundColor Green
} else {
    git init
    Write-Host "Git repository initialized" -ForegroundColor Green
}

Write-Host ""

# Step 3: Check Java
Write-Host "Step 3: Checking Java installation..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Write-Host "Java is installed: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "Java is not installed!" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Step 4: Add files to Git
Write-Host "Step 4: Adding files to Git..." -ForegroundColor Yellow
git add .
Write-Host "Files added to Git staging" -ForegroundColor Green

Write-Host ""

# Step 5: Create initial commit
Write-Host "Step 5: Creating initial commit..." -ForegroundColor Yellow
try {
    git commit -m "Initial commit - Transport tracking app with cloud build support"
    Write-Host "Initial commit created" -ForegroundColor Green
} catch {
    Write-Host "Commit may have failed (this is OK if already committed)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Local setup complete!" -ForegroundColor Green
Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. Create a GitHub repository:" -ForegroundColor White
Write-Host "   - Go to https://github.com/new" -ForegroundColor Gray
Write-Host "   - Name: transport-app" -ForegroundColor Gray
Write-Host "   - Do not initialize with README" -ForegroundColor Gray
Write-Host "   - Click Create repository" -ForegroundColor Gray
Write-Host ""
Write-Host "2. Connect to GitHub (replace YOUR_USERNAME):" -ForegroundColor White
Write-Host "   git remote add origin https://github.com/YOUR_USERNAME/transport-app.git" -ForegroundColor Gray
Write-Host "   git branch -M main" -ForegroundColor Gray
Write-Host "   git push -u origin main" -ForegroundColor Gray
Write-Host ""
Write-Host "3. Wait for GitHub Actions to build your APK" -ForegroundColor White
Write-Host "   (This takes 5-10 minutes)" -ForegroundColor Gray
Write-Host ""
Write-Host "4. Download your APK:" -ForegroundColor White
Write-Host "   - Go to your repository on GitHub" -ForegroundColor Gray
Write-Host "   - Click Actions tab" -ForegroundColor Gray
Write-Host "   - Click on the latest workflow run" -ForegroundColor Gray
Write-Host "   - Download app-debug artifact" -ForegroundColor Gray
Write-Host ""
Write-Host "For detailed instructions, see:" -ForegroundColor Cyan
Write-Host "   - CLOUD_BUILD_GUIDE.md (recommended)" -ForegroundColor White
Write-Host "   - MANUAL_BUILD_GUIDE.md (if you prefer local build)" -ForegroundColor White
Write-Host ""
Write-Host "Your project is ready for cloud build!" -ForegroundColor Green
