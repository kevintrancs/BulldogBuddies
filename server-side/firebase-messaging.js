
//firebase stuff, really don't know what i'm doing
var admin = require("firebase-admin");
var serviceAccount = require("client-side-69fc2-firebase-adminsdk-n9ioa-5e6ea9652c.json");

//Add the firebase stuff
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://client-side-69fc2.firebaseio.com"
  });


// This registration token comes from the client FCM SDKs.
var registrationToken = "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...";

// See the "Defining the message payload" section below for details
// on how to define a message payload.
var payload = {
  data: {
    score: "850",
    time: "2:45"
  }
};

// Send a message to the device corresponding to the provided
// registration token.
admin.messaging().sendToDevice(registrationToken, payload)
  .then(function(response) {
    // See the MessagingDevicesResponse reference documentation for
    // the contents of response.
    console.log("Successfully sent message:", response);
  })
  .catch(function(error) {
    console.log("Error sending message:", error);
  });

  //get my token 
  function getAccessToken() {
    return new Promise(function(resolve, reject) {
      var key = require('./client-side-69fc2-firebase-adminsdk-n9ioa-5e6ea9652c.json');
      var jwtClient = new google.auth.JWT(
        key.client_email,
        null,
        key.private_key,
        SCOPES,
        null
      );
      jwtClient.authorize(function(err, tokens) {
        if (err) {
          reject(err);
          return;
        }
        resolve(tokens.access_token);
      });
    });
  }