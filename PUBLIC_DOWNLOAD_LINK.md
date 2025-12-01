# 📦 How to Get Public Download Link for JAR

## Problem
GitHub Actions artifacts are **NOT publicly accessible**:
- ❌ Require authentication to download
- ❌ Expire after 7 days
- ❌ No direct raw download link

## Solution: GitHub Releases

GitHub Releases provide:
- ✅ **Permanent public download links**
- ✅ No authentication required
- ✅ Never expire
- ✅ Direct download URLs

---

## Option 1: Automatic Release (✅ RECOMMENDED)

I've updated your workflow to **automatically create releases**!

### How It Works:
1. Every time you push to `main` branch
2. GitHub Actions builds the JAR
3. Creates/updates a release called "latest"
4. Attaches the JAR file
5. You get a permanent public download link!

### Get the Link:
After the next build completes:

1. Go to: https://github.com/Prateek0xeo/Bajaj_Hiring/releases
2. Click on "latest" release
3. Right-click on `hiring-challenge-1.0.0.jar`
4. Select "Copy link address"

**Public Download Link Format:**
```
https://github.com/Prateek0xeo/Bajaj_Hiring/releases/download/latest/hiring-challenge-1.0.0.jar
```

---

## Option 2: Manual Release

If you want to create a release manually:

### Steps:
1. Wait for GitHub Actions build to complete
2. Download the JAR from Actions → Artifacts
3. Go to: https://github.com/Prateek0xeo/Bajaj_Hiring/releases
4. Click **"Create a new release"**
5. Click **"Choose a tag"** → Type `v1.0.0` → Create new tag
6. **Release title**: "Bajaj Hiring Challenge - v1.0.0"
7. **Description**: Add details about your project
8. **Attach files**: Drag and drop the JAR file
9. Click **"Publish release"**

### Result:
Public download link:
```
https://github.com/Prateek0xeo/Bajaj_Hiring/releases/download/v1.0.0/hiring-challenge-1.0.0.jar
```

---

## Push the Updated Workflow

To enable automatic releases, push the updated workflow:

```powershell
cd C:\Coding\Bajaj
git add .github/workflows/build.yml
git commit -m "Add automatic GitHub Release creation"
git push
```

This will:
1. ✅ Build the JAR
2. ✅ Create a "latest" release
3. ✅ Upload the JAR to the release
4. ✅ Give you a permanent public download link!

---

## After Push

1. **Wait 2-3 minutes** for build to complete
2. Go to: https://github.com/Prateek0xeo/Bajaj_Hiring/releases
3. Click "latest" release
4. **Copy the download link** for `hiring-challenge-1.0.0.jar`

---

## Public Download Link

Once the release is created, anyone can download with:

```bash
# Direct download with curl
curl -L -O https://github.com/Prateek0xeo/Bajaj_Hiring/releases/download/latest/hiring-challenge-1.0.0.jar

# Or with wget
wget https://github.com/Prateek0xeo/Bajaj_Hiring/releases/download/latest/hiring-challenge-1.0.0.jar
```

Or just share the link:
```
https://github.com/Prateek0xeo/Bajaj_Hiring/releases/download/latest/hiring-challenge-1.0.0.jar
```

---

## Submitting the Link

When asked for a "publicly downloadable link", provide:

```
https://github.com/Prateek0xeo/Bajaj_Hiring/releases/download/latest/hiring-challenge-1.0.0.jar
```

Anyone can download and run it with:
```bash
java -jar hiring-challenge-1.0.0.jar
```

---

## Next Steps

1. **Push the updated workflow** (commands above)
2. **Wait for build** to complete (~2-3 min)
3. **Check releases page**: https://github.com/Prateek0xeo/Bajaj_Hiring/releases
4. **Copy the download link**
5. **Submit the link** ✅

---

**Quick Command:**
```powershell
cd C:\Coding\Bajaj
git add .github/workflows/build.yml
git commit -m "Add automatic GitHub Release creation"
git push
```

Then check: https://github.com/Prateek0xeo/Bajaj_Hiring/releases
