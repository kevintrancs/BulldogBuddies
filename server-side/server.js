var express     = require('express');
var app         = express();
var bodyParser  = require('body-parser');
var morgan      = require('morgan');
var port = process.env.PORT || 8080; 
var bcrypt = require('bcrypt');
var routes = require('./routes');

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
