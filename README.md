# Pictrait

I built this app as a project to see how social media apps, and more generally client-server
applications, work. My attempt at replicating a simple photo-based social networking app is
the culmination of this. 

The main idea of the app is that users upload photos for their followers to see in a 
newsfeed-style view. 

### Features
- User
    - Login
    - Sign up
    - Perform account updates (eg: changing username or full name)
- Profile
    - Follow a new user
    - Unfollow a given user
    - View the profile of another user
    - Search for other users based on their given name
- Photo
    - View a newsfeed-style page with photos of users that are followed
    - Like a user's photo
    - Upload a new photo
    
### Technology Stack
#### Back-end API
- **Google App Engine**- standard environment used and hosted on the Google Cloud
- **Java 8**- used in conjunction with build management tool, Maven
- **Google Cloud Datastore**- to store the various entities in the app such as User, Photo,
Like, etc..
- **Google Cloud Storage**- to store and serve the user's photos they have uploaded
#### Front-end iOS Application (Swift)
- **Swift 3**- in conjunction with CocoaPods and with a build target of iOS 11.0

Basic structure outlined in the following picture:


![alt text](https://raw.githubusercontent.com/hineso11/Pictrait/master/Images/Data%20Flow%20Diagram.jpg 
"Stucture")

### Project Structure
The project can broadly be divided into two main sections:
- Back-end API
   - Files relating to this portion of the project can be found under the *pictrait-api* 
   directory
- Front-end iOS Application
    - Files relating to this portion of the project can be found under the *Pictrait* 
   directory