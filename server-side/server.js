var express     = require('express');
var app         = express();
var bodyParser  = require('body-parser');
var morgan      = require('morgan');
var port = process.env.PORT || 8080; 
var bcrypt = require('bcrypt');
var routes = require('./routes');
//var firebase = require('./firebase-messaging');

//firebase stuff, really don't know what i'm doing
var firebase= require("firebase-admin");
var request = require('request');
var serviceAccount = require("./client-side-69fc2-firebase-adminsdk-n9ioa-5e6ea9652c.json");

//API KEY
var API_KEY = "AAAAlI38-GQ:APA91bFR0jzeH8EBs3PjfVGzalfIDRyoWO_e7g6cT0VxGFctzcmOIjdxRcSIa-Cax9B-Kdq9LprEgmb_dK3zYj7NqGtITJhdnGapsNGiaOyGTi_oIx90hGV88VzhBP--ah1VHff-rAVh"

//Add the firebase stuff
firebase.initializeApp({
    credential: firebase.credential.cert(serviceAccount),
    databaseURL: "https://client-side-69fc2.firebaseio.com"
  });
ref = firebase.database().ref();

function listenForNotificationRequests() {
    var requests = ref.child('notificationRequests');
    requests.on('child_added', function(requestSnapshot) {
      var request = requestSnapshot.val();
      sendNotificationToUser(
        request.username, 
        request.message,
        function() {
          requestSnapshot.ref.remove();
        }
      );
    }, function(error) {
      console.error(error);
    });
  };
  
  function sendNotificationToUser(username, message, onSuccess) {
    request({
      url: 'https://fcm.googleapis.com/fcm/send',
      method: 'POST',
      headers: {
        'Content-Type' :' application/json',
        'Authorization': 'key='+API_KEY
      },
      body: JSON.stringify({
        notification: {
          title: message
        },
        to : '/topics/user_'+username
      })
    }, function(error, response, body) {
      if (error) { console.error(error); }
      else if (response.statusCode >= 400) { 
        console.error('HTTP Error: '+response.statusCode+' - '+response.statusMessage); 
      }
      else {
        onSuccess();
      }
    });
  }

  // start listening
  listenForNotificationRequests();


// use body parser so we can get info from POST and/or URL parameters
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
// use morgan to log requests to the console
app.use(morgan('dev'));
//---------------------------------------------
// API ROUTES  
//---------------------------------------------
app.use('/api', routes); 
// =======================
// start the server ======
// =======================
app.listen(port);
console.log('Listen at: ' + port);
