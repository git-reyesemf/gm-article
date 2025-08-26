#!/bin/bash

echo "🔨 Building application for Render deployment..."
echo "📊 Profile: test"
echo "🔗 Database: Aiven MySQL"
echo ""

# Clean and build
echo "🧹 Cleaning previous builds..."
./gradlew clean

echo "🔨 Building application..."
./gradlew build -x test

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo ""
    echo "📦 JAR file created:"
    ls -la build/libs/
    echo ""
    echo "🚀 Ready for Render deployment!"
    echo ""
    echo "💡 Next steps:"
    echo "   1. Push to your Git repository"
    echo "   2. Deploy on Render with profile 'test'"
    echo "   3. Render will use the existing Aiven database"
else
    echo "❌ Build failed!"
    exit 1
fi