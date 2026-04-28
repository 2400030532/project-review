# 🚀 Deploy to Render - Quick Start

## Your Project is Ready to Deploy!

✅ **Security**: CVE vulnerabilities fixed (mysql-connector-j 8.4.0)  
✅ **Database**: PostgreSQL configured  
✅ **Build**: Successfully compiles and packages  
✅ **Configuration**: render.yaml ready  

## Quick Deploy (5 minutes)

### Option A: Automatic Deploy via Render Dashboard

1. **Push to GitHub**:
   ```powershell
   cd "d:\project review\easyintern-project"
   git init
   git add .
   git commit -m "Ready for Render deployment"
   git branch -M main
   git remote add origin https://github.com/YOUR_USERNAME/easyintern.git
   git push -u origin main
   ```

2. **Deploy on Render**:
   - Go to https://render.com
   - Click **"New +"** → **"Blueprint"**
   - Connect GitHub & select your repo
   - Click **"Create New Blueprint Instance"**
   - Render auto-detects render.yaml and deploys all 3 services

3. **Wait for Deployment** (~5-10 minutes):
   - PostgreSQL database created
   - Backend (Java) compiled and deployed
   - Frontend (React) built and deployed

4. **Access Your App**:
   - **Frontend**: https://easyintern-frontend-v2.onrender.com
   - **Backend API**: https://easyintern-api-v2.onrender.com
   - **Health Check**: https://easyintern-api-v2.onrender.com/actuator/health

### Option B: Detailed Instructions

See [RENDER_DEPLOYMENT.md](RENDER_DEPLOYMENT.md) for comprehensive setup guide.

## What's Deployed

| Service | Tech Stack | URL |
|---------|-----------|-----|
| **Database** | PostgreSQL 15 | Internal connection |
| **API** | Java 17 + Spring Boot 3.2 | easyintern-api-v2.onrender.com |
| **Frontend** | React + Vite | easyintern-frontend-v2.onrender.com |

## Free Tier Notes

- API will sleep after 15 mins of inactivity (first request takes ~30s)
- Database pauses after 90 days
- 512MB RAM, 0.5 CPU per service
- Unlimited bandwidth

**Upgrade to Starter plan** in render.yaml for always-on performance (paid)

## Files Updated

- ✅ `render.yaml` - Enhanced with PostgreSQL and env vars
- ✅ `backend/pom.xml` - Added PostgreSQL driver
- ✅ `backend/Dockerfile` - Already configured
- ✅ `frontend/Dockerfile` - Already configured
- ✅ `backend/src/main/resources/application.yml` - Supports env vars
- ✅ `RENDER_DEPLOYMENT.md` - Full documentation

## Environment Variables (Auto-Set by Render)

```
SPRING_DATASOURCE_URL = postgresql://user:pass@...
SPRING_DATASOURCE_USERNAME = auto-generated
SPRING_DATASOURCE_PASSWORD = auto-generated
SPRING_DATASOURCE_DRIVER_CLASS_NAME = org.postgresql.Driver
SPRING_JPA_DATABASE_PLATFORM = org.hibernate.dialect.PostgreSQLDialect
SPRING_JPA_HIBERNATE_DDL_AUTO = update
APP_CORS_ALLOWED_ORIGINS = https://easyintern-frontend-v2.onrender.com
VITE_API_BASE_URL = https://easyintern-api-v2.onrender.com
GOOGLE_CLIENT_ID = your-google-client-id.apps.googleusercontent.com
VITE_GOOGLE_CLIENT_ID = your-google-client-id.apps.googleusercontent.com
SPRING_MAIL_USERNAME = your-email@example.com
SPRING_MAIL_PASSWORD = your-email-app-password
APP_MAIL_FROM = no-reply@easyintern.com
```

## Troubleshooting

**Can't push to GitHub?**
- Make sure you have Git installed: `git --version`
- Create personal access token: https://github.com/settings/tokens
- Use token as password when pushing

**Build fails after deployment?**
- Check Render dashboard build logs
- Verify pom.xml has all dependencies
- Check PostgreSQL is ready (wait 2-3 mins)

**Frontend not loading?**
- Open browser DevTools (F12)
- Check Console for errors
- Verify API URL in Network tab

**Google login or OTP not working?**
- Verify both Google client IDs match the same OAuth app
- Confirm backend mail credentials are valid
- Check Render logs for SMTP or token verification errors

## Next: Custom Domain (Optional)

In Render dashboard:
1. Go to easyintern-frontend service
2. Settings → Custom Domain
3. Add your domain and follow DNS setup

---

**Ready?** Push to GitHub and deploy! 🎉
