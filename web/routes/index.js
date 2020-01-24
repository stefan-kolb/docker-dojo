var express = require('express');
var rest = require('restler');
var async = require('async');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  var title = (req.query.title === undefined) ? 'Nara' : req.query.title;
  var artist = (req.query.artist === undefined) ? 'alt-J' : req.query.artist;

  async.parallel([
      function(callback){
        // get top songs
        console.log('Artist is ' + artist);

        rest.get('http://search:8080/api/artists/search?artist=' + artist).on('complete', function(data) {
          console.log('Artist ID is ' + data["id"]);
          rest.get('http://charts:8080/api/charts/' + data["id"]).on('complete', function(data) {
            console.log('Top songs are ' + JSON.stringify(data));
            callback(null, data);
          });
        });
      },
      function(callback){
          // get cover data
          console.log('Title is ' + title);

          rest.get('http://search:8080/api/tracks/search?title=' + title + '&artist=' + artist).on('complete', function(data) {
            console.log('Title ID is ' + data["id"]);

            rest.get('http://images:8080/api/covers/' + data["id"]).on('complete', function(data) {
              console.log('Cover image is ' + data["url"]);
              callback(null, data["url"]);
            });
          });
      }
  ],
  function(err, results){
      if(results[1] == undefined || results[1] == "") {
        results[1] = 'images/nocover.png';
      }
      // the results array will equal ['top tracks','cover_url']
      res.render('index', { title: 'Music Recommender', song: title, artist: artist, cover: results[1], charts: results[0] });
  });
});

module.exports = router;
