# EasyIntern Project - Render Deployment Guide

## ✅ Deployment Ready

Your EasyIntern project is fully configured for deployment to Render with the following improvements:

### Recent Updates

1. **CVE Security Fix** ✅
   - Updated `mysql-connector-j` from 8.0.33 → 8.4.0
   - Fixed HIGH severity CVE-2023-22102 (MySQL Connectors takeover vulnerability)

2. **Database Support** ✅
   - Added PostgreSQL driver to `pom.xml`
   - Configured backend to use PostgreSQL on Render
   - Falls back to H2 for local development

3. **Enhanced render.yaml** ✅
   - Added PostgreSQL database service
   - Configured environment variables for database connection
   - Updated CORS settings for production

## Deployment Steps

### Step 1: Push Code to GitHub/GitLab

```bash
# Initialize git if not already done
git init
git add .
git commit -m "chore: prepare for Render deployment with CVE fixes and PostgreSQL support"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/easyintern.git
git push -u origin main
```

### Step 2: Deploy to Render

1. Go to [render.com](https://render.com) and sign up for a free account
2. Click **"New +"** → **"Blueprint"**
3. Connect your GitHub account
4. Select your repository
5. Render will automatically detect `render.yaml` and show the services:
   - **easyintern-db**: PostgreSQL database (free tier, 90-day auto-pause)
   - **easyintern-api**: Java backend API (free tier)
   - **easyintern-frontend**: React frontend static site (free tier)
6. Click **"Create New Blueprint Instance"**

### Step 3: Monitor Deployment

Render will:
1. Create the PostgreSQL database
2. Build and deploy the backend API
3. Build and deploy the frontend
4. Configure environment variables automatically

Monitor progress in the Render dashboard. Typical deployment time: **5-10 minutes**

## Services After Deployment

Once deployed, you'll have:

| Service | Type | URL | Status |
|---------|------|-----|--------|
| **Database** | PostgreSQL | Internal only | Free tier (90-day auto-pause) |
| **API** | Java Web | `https://easyintern-api.onrender.com` | Free tier (auto-sleep after 15 min inactivity) |
| **Frontend** | Static Site | `https://easyintern-frontend.onrender.com` | Free tier (always on) |

### Free Tier Limitations

- API auto-sleeps after 15 minutes of inactivity (first request will take ~30s)
- Database auto-pauses after 90 days
- Limited compute resources

## Production Readiness

To upgrade to production-grade performance, upgrade services from **free** to **starter** or **pro** plans in `render.yaml`:

```yaml
plan: starter  # Instead of: plan: free
```

## Configuration Details

### Backend (Java/Spring Boot)

**Port**: 8080  
**Health Check**: `/actuator/health`  
**Database**: PostgreSQL (auto-configured from environment variables)

Environment Variables (auto-set by Render):
- `SPRING_DATASOURCE_URL`: PostgreSQL connection string
- `SPRING_DATASOURCE_USERNAME`: Auto-generated user
- `SPRING_DATASOURCE_PASSWORD`: Auto-generated password
- `SPRING_JPA_DATABASE_PLATFORM`: PostgreSQL dialect
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: `update` (auto-creates tables)

### Frontend (React + Vite)

**Port**: 5173 (during build), served as static site  
**Build Output**: `dist/` directory  
**API URL**: Automatically set to `https://easyintern-api.onrender.com`

### Database (PostgreSQL)

**Version**: 15  
**Auto-backup**: Enabled  
**Connection**: Automatic via environment variables

## Testing After Deployment

1. **Check API Health**:
   ```
   https://easyintern-api.onrender.com/actuator/health
   ```

2. **Check Frontend**:
   ```
   https://easyintern-frontend.onrender.com
   ```

3. **Test API Endpoint** (example):
   ```
   https://easyintern-api.onrender.com/api/v1/internships
   ```

## Troubleshooting

### "Service won't start"
- Check build logs in Render dashboard
- Ensure all dependencies are in `pom.xml`
- Verify Java version matches in Dockerfile (Java 17)

### "Frontend not connecting to API"
- Verify CORS is enabled: Check `application.yml` for `APP_CORS_ALLOWED_ORIGINS`
- Check browser console for CORS errors
- Ensure backend is deployed and running

### "Database connection fails"
- Wait 2-3 minutes for PostgreSQL to initialize
- Check `SPRING_DATASOURCE_URL` environment variable is set
- Verify hibernation settings in `application.yml`

### "Slow first request"
- Normal on free tier - API auto-sleeps
- First request after inactivity takes ~30s to wake up
- Upgrade to paid tier to prevent auto-sleep

## Next Steps

1. ✅ Push code to GitHub
2. ✅ Create Render account
3. ✅ Deploy using Blueprint (render.yaml)
4. ✅ Test the deployed services
5. (Optional) Set up custom domain in Render settings
6. (Optional) Upgrade to paid plans for production

## Rollback

If something goes wrong, you can:
1. Delete the services in Render dashboard
2. Fix the issues locally
3. Push changes to GitHub
4. Redeploy

## Support Resources

- Render Documentation: https://render.com/docs
- Spring Boot on Render: https://render.com/docs/deploy-spring-boot
- PostgreSQL on Render: https://render.com/docs/databases

---

**Questions or Issues?** Check the Render dashboard logs or GitHub issues for error details.
