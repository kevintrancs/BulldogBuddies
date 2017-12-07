var express = require('express');
var apiRoutes = express.Router();
var User   = require('./models/user'); 
var jwt    = require('jsonwebtoken'); 
var config = require('./config');
var mongoose    = require('mongoose');
var app         = express();
var Status = require("mongoose-friends").Status;
mongoose.connect(config.database); //Crossing my fingers here
app.set('gina_secret', config.secret); 

/**
 * @GET /myRequests - Checks what pending friend requests you have
 * @param String: Token - authenticate who you are
 * @return success: {date, {String} Status.PENDING, {String}_id, User
 */
apiRoutes.get('/myRequests',function(req, res){
    var token = req.headers['x-access-token'];    
    if(token){
        jwt.verify(token, app.get('gina_secret'), function(err, decoded) {  
            if(err){
                return res.json({ success: false, 
                    message: 'Failed to authenticate token.' 
                });   
            }
            User.getFriends(decoded.user, {"myFriends.status": Status.Pending}, function(err, result){
                return res.json({
                    success:true,
                    friends: result
                });
            });
         
        });    
    } 
    else{
        return res.json({ success: false, 
            message: 'No Token' 
        });   
    }
});


/**
 * 
 * @GET /friends  - Gets your ACCEPTED friends
 * @param String: Token - authenticate who you are
* @return success: [{date, {String} Status.PENDING, {String}_id, User]
 */
apiRoutes.get('/friends',function(req, res){
    var token = req.headers['x-access-token'];    
    if(token){
        jwt.verify(token, app.get('gina_secret'), function(err, decoded) {  
            if(err){
                return res.json({ success: false, 
                    message: 'Failed to authenticate token.' 
                });   
            } 
                    User.getFriends(decoded.user,{"myFriends.status": Status.Accepted},function (err, friendships) {
                    if(err)
                        throw err;
                    return res.json({
                        success:true,
                        friends: friendships})
                  });      
        });    
    } 
    else{
        return res.json({ success: false, 
            message: 'No Token' 
        });   
    }
})

/**
 * @POST /requestFriend - Sends friend request
 * @param String:r_friend - name of the friend you want to send
 * @return success: {boolean, String}
*/
apiRoutes.post('/requestFriend', function(req,res){
    var search_friend = req.body.name;
    var token = req.headers['x-access-token'];    
    if(token){
        jwt.verify(token, app.get('gina_secret'), function(err, decoded) {  
            if(err){
                return res.json({ success: false, 
                    message: 'Failed to authenticate token.' 
                });   
            } 
            User.findOne({name: decoded.user.name}, function(err, self){
                if(err)
                    throw error;
                if(self){
                    User.findOne({name: search_friend}, function(err, friend){
                        if(err)
                            throw error;
                        if(friend){
                            User.requestFriend(self._id, friend._id,function(err, friends){
                                if(err)
                                    throw err;
                                return res.json({
                                    success: true,
                                    message: "Request sent :)"
                                });
                            });
                        }
                    });
                }
                else{                    
                    res.json({
                        success: false,
                        message: "failure"
                    });
                }
            });
        });  
    }
    else{
        return res.json({
            success:false,
            message: "I don't know how you got here, but this is not you friend."
        })
    }
})

/**
 * @POST /authenticate - Logins user in
 * @param {String: name, String: password}
 * @return {boolean, String:message, String:token, User:user}
 * 
 */
apiRoutes.post('/authenticate', function(req, res) {
      var user_to_find = req.body.name;
      var user_password_entered = req.body.password;

      User.findOne({name: user_to_find}, function(err, user) {
        if (err) 
            throw err;
        if (!user) {
          res.json({ success: false, message: 'Student does not EXIST, sorry.' });
        } 
        else if (user) {
            user.comparePassword(user_password_entered, function(err, isMatch) {
                if (err) 
                    throw err;
                if(isMatch){
                    const payload = {
                        user
                      };
                          var token = jwt.sign(payload, app.get('gina_secret'), {expiresIn: '10h'
                          });
                          res.json({
                            success: true,
                            message: user.name,
                            token: token,
                            user: payload
                          });
                }
                else{
                    res.json({
                        success:false,
                        message: 'Failure to authenticate'
                    });
                }
            });
        }
      });
    });

/**
 * @POST /register - Register user
 * @param {String name, password, department, phone, Number[]: survey_results}
 * @return User: user
 */
apiRoutes.post('/register', function(req, res){
         var user_to_register = req.body.name;    
        var new_user;
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
        User.findOne({name: user_to_register}, function (err, success) {
            if (err) {
                console.log(err);
                res.send(err);
            }
            else {
                if (success == null) {
                    var new_user = new User({
                        name: req.body.name,
                        password: req.body.password,
                        department: req.body.department,
                        phone: req.body.phone,
                        survey_results: req.body.survey_data,
                    });
                    new_user.save(function(err){
                        if (err) 
                            throw err;
                        else{
                            res.json({ 
                                success:true,
                                user: new_user,
                                message: "Successful Creation"});
                        }
                    });
                } else {
                    res.send("Student already present");
                }
            }
        })
    });

/**
 * @GET /profile/:id - Gets data about loggedin user
 * @param String: token
 * @return {boolean, String:message, User:user}
 */
apiRoutes.get('/profile/:id', function(req, res){
    var token = req.headers['x-access-token'];
    if(token){
        jwt.verify(token, app.get('gina_secret'), function(err, decoded) {  
            if(err){
                return res.json({ success: false, 
                    message: 'Failed to authenticate token.' 
                });   
            } 
            if(decoded.user.name == req.params.id){
                return res.json({
                    success:true,
                    message: "Welcome " + decoded.user.name,
                    user: decoded.user
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
/**
 * @GET /
 */
apiRoutes.get('/', function(req, res) {
  res.json({ message: 'Kevin Tran Default Testing Route localhost:8080/api/' });
});

/**
 * @GET /users
 * @return Users[]
 */
apiRoutes.get('/users', function(req, res) {
  User.find({}, function(err, all_users) {
    res.json({
        success: true,
        users: all_users});
  });
});  

/**
 * @GET /getMatches - Finds matches everytime searched based on survey results
 * This function is janky beyond belief, it works I guess, needs better method and matching.
 * This is just dumb af
 * TODO: FIX IT ALL
 * 
 * @param {String:token}
 * @return {boolean, String:message, matches[User]}
 */
apiRoutes.get('/getMatches', function(req, res){
    var token = req.headers['x-access-token'];  
    var a = [];
    if(token){
        jwt.verify(token, app.get('gina_secret'), function(err, decoded) {  
            if(err){
                return res.json({ success: false, 
                    message: 'Failed to authenticate token.' 
                });   
            }
            User.find({}, function(err, users){
                for(var i = 0; i < users.length; i++){
                    var matches = 0;
                    for(var j = 0; j < decoded.user.survey_results.length-1; j++){
                        if(users[i].survey_results[j] == decoded.user.survey_results[j]){
                            matches++;
                        }
                    }
                    if(matches >2 & (decoded.user.name != users[i].name)) 
                        a.push(users[i])
                }
        });
        User.findById(decoded.user._id).populate('matches').exec(function (err, self) {
          if (err)
            throw err;
          else {// FLAW SAVE BEFORE THE POPULATE move when you have time
            self.matches = a;
            self.save(function(err, zz){
                  if(err)
                    throw err;
              });
            return res.json({
                success: true,
                message: "This works?",
                users: a
            });
          }
      });
        });      
    } 
});

module.exports = apiRoutes;