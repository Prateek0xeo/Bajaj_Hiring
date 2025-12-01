# 🚀 Build JAR Using GitHub Actions (No Local JDK Required!)

## Overview
This guide shows you how to build the JAR file using GitHub's servers instead of your local machine. **No JDK installation required on your computer!**

---

## Step-by-Step Instructions

### Step 1: Create a GitHub Repository

1. Go to https://github.com
2. Click the **"+"** icon → **"New repository"**
3. Name it: `bajaj-hiring-challenge`
4. Keep it **Private** (for your code security)
5. **Do NOT** initialize with README (we already have files)
6. Click **"Create repository"**

---

### Step 2: Push Your Code to GitHub

Open PowerShell in `C:\Coding\Bajaj` and run these commands:

```powershell
# Initialize git repository
git init

# Add all files
git add .

# Commit the code
git commit -m "Initial commit - Bajaj Hiring Challenge"

# Add your GitHub repository as remote (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/bajaj-hiring-challenge.git

# Push to GitHub
git push -u origin master
```

**Note**: Replace `YOUR_USERNAME` with your actual GitHub username!

If you don't have git installed:
- Download from: https://git-scm.com/download/win
- Or, manually upload via GitHub web interface (see Option B below)

---

### Step 3: GitHub Builds Your JAR Automatically! 🎉

Once you push the code:

1. GitHub Actions will **automatically start building**
2. Go to your repository on GitHub
3. Click the **"Actions"** tab
4. You'll see the build running (usually takes 2-3 minutes)
5. Wait for the green ✅ checkmark

---

### Step 4: Download the Built JAR

1. In the **Actions** tab, click on the completed workflow run
2. Scroll down to **"Artifacts"** section
3. Click **"hiring-challenge-jar"** to download
4. Extract the ZIP file
5. You'll find `hiring-challenge-1.0.0.jar` inside!

---

## Alternative: Manual Upload (No Git Required)

### Option B: Upload Files Manually

If you don't want to use git:

1. Create a new repository on GitHub (as in Step 1)
2. Click **"uploading an existing file"** link
3. **Drag and drop** all files from `C:\Coding\Bajaj` into the upload area
   - Make sure to include the `.github` folder!
4. Click **"Commit changes"**
5. GitHub Actions will start building automatically

---

## How to Trigger Manual Build

The workflow is set to run on:
- ✅ Every push to the repository
- ✅ Manual trigger (workflow_dispatch)

To manually trigger:
1. Go to **Actions** tab
2. Select **"Build JAR"** workflow
3. Click **"Run workflow"** button
4. Select branch (usually `master` or `main`)
5. Click green **"Run workflow"** button

---

## What Happens Behind the Scenes

GitHub Actions will:
1. ✅ Set up Ubuntu Linux server
2. ✅ Install JDK 8 automatically
3. ✅ Cache Maven dependencies for faster builds
4. ✅ Run `./mvnw clean package -DskipTests`
5. ✅ Upload the JAR file as a downloadable artifact
6. ✅ Keep the artifact for 7 days

**Total time**: Usually 2-4 minutes ⚡

---

## After Downloading the JAR

Once you have the JAR file:

```powershell
# Navigate to where you saved the JAR
cd C:\Downloads  # or wherever you extracted it

# Run the application (only JRE needed, not JDK!)
java -jar hiring-challenge-1.0.0.jar
```

**Note**: You only need **JRE** (Java Runtime) to RUN the JAR, which you already have!

---

## Troubleshooting

### "Actions tab not visible"
- Make sure you're logged into GitHub
- Check repository settings → Actions → Enable Actions

### "Build failed"
- Click on the failed workflow
- Check the logs for errors
- Usually it's a typo in file paths

### "Can't download artifact"
- Artifacts expire after 7 days
- Re-run the workflow to generate a fresh one

### "Git not installed"
- Use Option B (manual upload)
- Or download Git: https://git-scm.com/download/win

---

## Quick Reference Commands

```powershell
# If you need to update code and rebuild:
git add .
git commit -m "Updated configuration"
git push

# GitHub will automatically rebuild!
```

---

## File Structure Check

Make sure these files are in your repository:
```
bajaj-hiring-challenge/
├── .github/
│   └── workflows/
│       └── build.yml          ← This triggers the build!
├── src/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── .mvn/
```

---

## Benefits of This Approach

✅ **No JDK installation** required on your machine  
✅ **Consistent environment** - builds on clean Linux server  
✅ **Version controlled** - all code changes tracked  
✅ **Automated** - builds on every push  
✅ **Free** - GitHub Actions is free for public and small private repos  

---

## Ready to Start?

1. Create GitHub repository
2. Push your code (or upload manually)
3. Wait 2-3 minutes for build
4. Download JAR from Actions → Artifacts
5. Run with `java -jar hiring-challenge-1.0.0.jar`

**Questions?** Let me know! 🚀
