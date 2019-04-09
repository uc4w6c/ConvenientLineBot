#!/bin/sh

curl=`cat <<EOS
curl
 -v
 -i
 -X POST
 http://localhost:8080/linebot/create
 -H 'Content-Type: application/json'
 -H 'X-Line-Signature: 3q8QXTAGaey18yL8FWTqdVlbMr6hcuNvM4tefa0o9nA='
 -d '{
  "events": [
    {
      "replyToken": "0f3779fba3b349968c5d07db31eab56f",
      "type": "message",
      "timestamp": 1462629479859,
      "source": {
        "type": "user",
        "userId": "U4af4980629..."
      },
      "message": {
        "id": "325708",
        "type": "text",
        "text": "Hello, world"
      }
    },
    {
      "replyToken": "8cf9239d56244f4197887e939187e19e",
      "type": "follow",
      "timestamp": 1462629479859,
      "source": {
        "type": "user",
        "userId": "U4af4980629..."
      }
    }
  ]
}'
EOS`
eval ${curl}
