
var express     = require('express');
var app         = express();
var bodyParser  = require('body-parser');
var morgan      = require('morgan');
var mongoose    = require('mongoose');
var bcrypt = require('bcrypt');
var jwt    = require('jsonwebtoken'); 
var config = require('./config');
var User   = require('./models/user'); 
var port = process.env.PORT || 8080; 
mongoose.connect(config.database); //Crossing my fingers here
app.set('gina_secret', config.secret); 

// use body parser so we can get info from POST and/or URL parameters
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// use morgan to log requests to the console
app.use(morgan('dev'));
var salt = bcrypt.genSaltSync(10);

//---------------------------------------------
// API ROUTES 
// Didn't feel like different module for routers, maybe sometime in the future 
//---------------------------------------------
var apiRoutes = express.Router(); 

//Basically Login
//Returns JWT for further use sometime
apiRoutes.post('/authenticate', function(req, res) {
    
      // find the user
      User.findOne({
        name: req.body.name
      }, function(err, user) {
    
        if (err) 
            throw err;
    
        if (!user) {
          res.json({ success: false, message: 'Student does not EXIST, sorry.' });
        } 
        else if (user) {
          // check if password matches
          if (user.password != bcrypt.hashSync(req.body.password, salt)) {
            res.json({ success: false, message: 'Authentication failed. Wrong password.' });
          } else {
        // if user is found and password is right
        // create a token with only our given payload
        const payload = {
          name: user.name
        };
            var token = jwt.sign(payload, app.get('gina_secret'), {expiresIn: 3600 //a week???
            });
            // return the information including token as JSON
            res.json({
              success: true,
              message: user.name,
              token: token
            });
          }   
        }
      });
    });

//Register Route
    apiRoutes.post('/register', function(req, res){

        var passwordToSave = bcrypt.hashSync(req.body.password, salt) //crypt this pass
        //Creating new user here fam
        var new_user = new User({
            name: req.body.name,
            password: passwordToSave
        });
        /**
         * Implmement this after testing is done so i'm not frustrated and shit
         
            var splitString = req.body.name.split("@");
            if(splitString[1] == "zagmail.gonzaga.edu"){
            User.findOne({name: req.body.name}, function (err, success) {
                if (err) {
                    console.log(err);
                    res.send(err);
                }
                else {
                    console.log(success);
                    if (success == null) {
                        new_user.save(function(err){
                            if(err)
                                throw err;
                            res.json({
                                success: true,
                                message: 'Created user ' + req.body.name
                            });
                        });
        
                    } else {
                        res.send("Student already present");
                    }
        
                }
            })
         }
         */
        User.findOne({name: req.body.name}, function (err, success) {
            if (err) {
                console.log(err);
                res.send(err);
            }
            else {
                console.log(success);
                if (success == null) {
                    new_user.save(function(err){
                        if(err)
                            throw err;
                        res.json({
                            success: true,
                            message: 'Created user ' + req.body.name
                        });
                    });
    
                } else {
                    res.send("Student already present");
                }
    
            }
        })
    });
    
apiRoutes.get('/profile/:id', function(req, res){
    var token = req.headers['x-access-token'];
    if(token){
        jwt.verify(token, app.get('gina_secret'), function(err, decoded) {  
            if(err){
                return res.json({ success: false, 
                    message: 'Failed to authenticate token.' 
                });   
            } 
            if(decoded.name == req.params.id){
                return res.json({
                    success:true,
                    message: decoded.name
                })
            }
            else {
                return res.json({
                    success:false,
                    message: "I don't know how you got here, but this is not you friend."
                })
            }
        })      
    }
    
});

apiRoutes.get('/', function(req, res) {
  res.json({ message: 'Kevin Tran Default Testing Route localhost:8080/api/' });
});

apiRoutes.get('/users', function(req, res) {
  User.find({}, function(err, users) {
    res.json(users);
  });
});   

// apply the routes to our application with the prefix /api
app.use('/api', apiRoutes);

// =======================
// start the server ======
// =======================
app.listen(port);
console.log('Listen at: ' + port);