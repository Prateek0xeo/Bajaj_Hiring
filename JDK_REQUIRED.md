# ⚠️ Java Development Kit (JDK) Required

## Issue Detected
Your system currently has **Java Runtime Environment (JRE) 1.8** installed, but Maven requires the **Java Development Kit (JDK)** to compile Java code.

- ✅ **JRE** (Java Runtime) - Can RUN Java programs
- ❌ **JDK** (Java Development Kit) - Can COMPILE and RUN Java programs ← **You need this!**

---

## Quick Fix Options

### Option 1: Install JDK 8 (Recommended for this project)

1. **Download JDK 8**:
   - Oracle JDK 8: https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html
   - OR OpenJDK 8: https://adoptium.net/temurin/releases/?version=8

2. **Install** to default location (e.g., `C:\Program Files\Java\jdk1.8.0_xxx`)

3. **Set JAVA_HOME** (Permanent):
   - Open System Properties → Environment Variables
   - Add new System Variable:
     - Name: `JAVA_HOME`
     - Value: `C:\Program Files\Java\jdk1.8.0_xxx` (your actual path)
   - Add to Path: `%JAVA_HOME%\bin`

4. **Verify**:
   ```cmd
   java -version
   javac -version
   ```

5. **Build the project**:
   ```cmd
   cd C:\Coding\Bajaj
   .\mvnw.cmd clean package
   ```

---

### Option 2: Install JDK 17+ (Better Long-term)

If you want to use the original Spring Boot 3.x version:

1. **Download JDK 17+**:
   - Oracle JDK 17: https://www.oracle.com/java/technologies/downloads/#java17
   - OR OpenJDK 17: https://adoptium.net/temurin/releases/?version=17

2. **Install and set JAVA_HOME** (same as Option 1)

3. **Revert to Spring Boot 3.x**:
   - I'll provide commands to revert the compatibility changes
   - Spring Boot 3.x is more modern and recommended

---

## Temporary Workaround (This Session Only)

If you want to build right now without permanent install:

```powershell
# Set JAVA_HOME temporarily in PowerShell
$env:JAVA_HOME = "C:\path\to\your\jdk"
.\mvnw.cmd clean package
```

**Note**: You'll need to set this every time you open a new PowerShell window.

---

## Current Project Status

I've already made the code Java 8 compatible:
- ✅ Changed Spring Boot from 3.2.0 → 2.7.18
- ✅ Changed Java version from 17 → 8
- ✅ Replaced modern Java syntax with Java 8 compatible code
- ✅ Your details are updated (Prateek Kumar, 22BCE2300)

**What's missing**: Just the JDK installation!

---

## Alternative: Use Online Build Service

If you can't install JDK right now, you could:

1. **Upload to GitHub**
2. **Use GitHub Actions** to build the JAR
3. **Download the built JAR**

Would you like me to create a GitHub Actions workflow file for this?

---

## Recommended Next Steps

1. **Install JDK 8** (fastest path to building)
   - Download from: https://adoptium.net/temurin/releases/?version=8
   - Install to default location
   - Set JAVA_HOME environment variable

2. **Build the project**:
   ```cmd
   cd C:\Coding\Bajaj
   .\mvnw.cmd clean package
   ```

3. **Run the application**:
   ```cmd
   java -jar target\hiring-challenge-1.0.0.jar
   ```

---

## Need Help?

Let me know which option you'd like to pursue:
- **Option A**: I can guide you through JDK 8 installation step-by-step
- **Option B**: I can help you upgrade to JDK 17 and revert to Spring Boot 3.x
- **Option C**: I can create a GitHub Actions workflow to build remotely

Choose based on your preferences! 🚀
