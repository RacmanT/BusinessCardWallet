<img src="/app/src/main/res/drawable/app_logo.png"  width="200" height="200"  />

# BusinessCardWallet

## Table of Contents

- [Introduction](#introduction)
- [Overview](#overview)
- [Structure](#structure)
- [Dependencies](#dependencies)


### Introduction

Think how shameful was for the poor Patrick Bateman, the American Psycho, when he [found out](https://www.youtube.com/watch?v=cISYzA36-ZY) that his
business card, is not that good as he thought. This simple android phone application aims to avoid these situations, by providing a digital business card wallet.

---

### Overview

<div align="center" >
<img src="/Screenshots/Home.png"  width="200" height="400" />

<img src="/Screenshots/ContactList.png"  width="200" height="400"  />

<img src="/Screenshots/Settings.png"   width="200" height="400" />
</div>  

The application has the following functionalities:

- [x] make an account and keep your credentials secure using Firebase Authentication
- [x] sync your data across every device using Firebase Firestore
- [x] allows you to decode others Business Card using a QR code scanner
- [x] allows you to send you Business card as an QR code image
- [x] allows you to read a Business card from a image containing an QR code
- [x] allows you to print the QR code associated with your Business card
- [x] manage a contact list and search through them
- [x] check your contacts business card 
- [x] change your info or delete your account


---


### Structure

The following is a brief guide on how the project works. The structure will be explained during the presentation.
```

BusinessCardWallet
    │    
    ├── AuthenticationActivity
    |         ├── LoginFragment
    |         └── RegisterFragment
    │   
    ├── MainActivity
    |         ├── BusinessCardFragment
    |         └── ContactListFragment
    │  
    ├── ContactInfoActivity  
    |         └── BusinessCardFragment
    │    
    ├── SettingsActivity
    |         ├── SettingsFragment
    |         ├── EditAccountFragment
    |         └── EditUserFragment
    |
    └── CustomScannerActivity
    
 ```

The `AuthenticationActivity` is a container for the `Login` fragment and the `Registration` fragment.
Once the user has authenticate is redirected to the `MainActivity` which will retrieve the user updated data (user info and his contact list) and display them
through a tab layout, consisting in an `BusinessCardFragment` and a `ContactListFragment`. 
The QR codes is given by an AES encrypted json string, containing an user data. The `CustomScannerActivity` takes care of decrypting and 
decoding the data, which are then used to save the user to the contacts and display his business card. Once displayed, the user can interact with the
contact's business card info by tapping on them (for instance it will start the phone dealer when tapping on the contact phone number).   
The logged user can change his information (both of the account and of the business card) by using the `SettingsActivity` and its fragments.
From here he can also change the appearance of the application, log out or delete his account.

---


### Dependencies

The application uses some external API:
- [Zxing](https://github.com/journeyapps/zxing-android-embedded) in order to generate and scan QR codes
- [Firebase](https://firebase.google.com/) to store users credentials and data
