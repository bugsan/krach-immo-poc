http://krach-immo.appspot.com/chart.html
http://krach-immo.appspot.com/api/annonces

curl -v http://krach-immo.appspot.com/chart.html
curl -v -H "Accept-Encoding: gzip" http://krach-immo.appspot.com/api/annonces
curl -v -X POST -H "Content-Type: application/json" -d 'hello' http://krach-immo.appspot.com/api/indexes/foo
curl -v http://krach-immo.appspot.com/api/indexes/foo/search?q=
