# BulldogBuddies
This is the final project for CPSC 321. 
An android mobile that allows students in the University to connect with each other.

## Getting Started & Reqs
Have npm and nodejs, and android studio.
MongoDB account, prefer from https://mlab.com/ create a free account.

### Installing & Usage
```
npm install
```
This will set up deps
```
node sever
```
Server sits on localhost:8080

Create a config.js file in server-side dir, and add 
```
module.exports = {
        'secret': 'your secrethere',
        'database': 'mongodb://YOURDBSTUFF'
    };
```

#### Authors
Kevin Tran & Nicole Howard

