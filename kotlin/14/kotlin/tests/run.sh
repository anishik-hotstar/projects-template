#!/bin/bash
echo "inside shell"
sleep 7 
sed -i -e "s+http://localhost:8080+$2+g" $1
sed -i -e "s+something.com+$3+g" $1
echo "replaced props"
zip -r features.zip features/
curl --location --request POST $4 \
--form 'featureFile=@"features.zip"' \
--form 'propFile=@"test.properties"'
