# 🔍 How to Check Build Logs on GitHub

## The Build is Failing

The GitHub Actions build is failing very quickly (within 2-5 seconds), which suggests an early configuration issue. To see the exact error, you need to view the detailed logs.

## Steps to View Build Logs

### 1. Sign in to GitHub
Go to https://github.com and sign in with your account

### 2. Go to Your Repository
Navigate to: https://github.com/Prateek0xeo/Bajaj_Hiring

### 3. Click the "Actions" Tab
You'll see a list of workflow runs

### 4. Click on the Failed Build
Look for the latest run: "Fix: Use Maven directly instead of wrapper to avoid line ending issues"
- It will have a red ❌ mark
- Click on it

### 5. Click on the "build" Job
On the left sidebar, you'll see a "build" job
- Click on it to expand

### 6. Read the Logs
Scroll through the logs to find the error message
- Look for red text or "ERROR" messages
- The error will likely be in the "Build with Maven" step

## Common Issues and Solutions

### Issue 1: "Could not find pom.xml"
**Solution**: The pom.xml file wasn't pushed correctly
```powershell
cd C:\Coding\Bajaj
git add pom.xml
git commit -m "Add pom.xml"
git push
```

### Issue 2: "Compilation failure" or Java syntax errors
**Solution**: There might be an issue with the Java 8 compatible code
- Check if all Java files were pushed correctly
- Make sure there are no text block syntax (""") remaining

### Issue 3: "Plugin execution not covered by lifecycle"
**Solution**: Maven plugin configuration issue
- This is less likely with our simple pom.xml

## Alternative: Share the Error with Me

Once you're signed in and can see the error:

1. **Take a screenshot** of the error in the logs
2. **Or copy the error text**
3. **Share it**  with me

Then I can provide a specific fix!

## Manual Build Instructions (Fallback)

If GitHub Actions continues to fail, here's what you can do:

### Option A: Install JDK  and Build Locally
See `JDK_REQUIRED.md` for instructions

### Option B: Use a Cloud IDE
1. Go to https://github.com/Prateek0xeo/Bajaj_Hiring
2. Press `.` (period key) to open GitHub.dev
3. Or click "Code" → "Codespaces" → "Create codespace"
4. In the terminal:
   ```bash
   mvn clean package -DskipTests
   ```
5. Download the JAR from `target/hiring-challenge-1.0.0.jar`

## Next Steps

1. ✅ Sign in to GitHub
2. ✅ Go to Actions tab
3. ✅ Click on the failed build
4. ✅ Read the error logs
5. ✅ Share the error with me or try the solutions above

---

**Quick Link**: https://github.com/Prateek0xeo/Bajaj_Hiring/actions/runs/19821027872
