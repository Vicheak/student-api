read -p "Enter your commit message : " message
git add .
git commit -m "$message"
git push -u origin main
