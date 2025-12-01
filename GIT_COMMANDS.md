# Git Setup Quick Commands

## First Time Setup (One-time only)

```powershell
# Configure your identity
git config --global user.name "Prateek Kumar"
git config --global user.email "Prateekkumar2004@gmail.com"
```

## Push to GitHub (Step-by-step)

```powershell
# 1. Navigate to project directory
cd C:\Coding\Bajaj

# 2. Initialize git repository
git init

# 3. Add all files
git add .

# 4. Commit the code
git commit -m "Bajaj Hiring Challenge - Initial commit"

# 5. Add remote (REPLACE 'YOUR_USERNAME' with your GitHub username!)
git remote add origin https://github.com/YOUR_USERNAME/bajaj-hiring-challenge.git

# 6. Push to GitHub (might ask for GitHub credentials)
git push -u origin master
```

## If Push Fails with "master" Branch

Some repos use "main" instead:

```powershell
git branch -M main
git push -u origin main
```

## Authentication

When pushing, GitHub will ask for authentication:

### Option 1: Personal Access Token (Recommended)
1. Go to GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Click "Generate new token (classic)"
3. Give it a name: "Bajaj Project"
4. Select scopes: `repo` (full control)
5. Click "Generate token"
6. **Copy the token** (you won't see it again!)
7. When git asks for password, paste the token

### Option 2: GitHub CLI
```powershell
# Install GitHub CLI
winget install --id GitHub.cli

# Login
gh auth login
```

## Quick Reference

```powershell
# Check status
git status

# View remote
git remote -v

# Update after making changes
git add .
git commit -m "Description of changes"
git push
```

## Undo Last Commit (if needed)

```powershell
# Undo commit but keep changes
git reset --soft HEAD~1

# Undo commit and changes
git reset --hard HEAD~1
```
