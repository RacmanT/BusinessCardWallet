# BusinessCardWallet

## Table of Contents

- [Introduction](#introduction)
- [Overview](#overview)
- [Structure](#structure)


### Introduction

Think how easier would have been for the poor Patrick Bateman, the American Psycho, when he [found out](https://www.youtube.com/watch?v=cISYzA36-ZY) that his
business card, is not that good as he thought. This simple android phone application aims to solve this problem, by providing a business card wallet.

---

### Overview

The application has the following functionalities:

- [x] make an account keep you credentials secure using Firebase Authentication
- [x] sync your data accross every device using Firebase Firestore
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

 The `AuthenticationActivity` is a container for the `Login` fragment and the `Registraton` fragment. 
Once the user has authenticate is redirected to the `MainActivity` which will retreive the user updated data (user info and his contact list) and display them 
through a tab layout, consisting in an `BusinessCardFragment` and a `ContactListFragment`. 
The QR codes is given by an AES encrypted json string, containing an user data. The `CustomScannerActivity` takes care of decrypting and 
decoding the data, which are then used to save the user to the contacts and display his business card. Once displayed, the user can interract with the 
contact's business card info by tapping on them (for instance it will start the phone dealer when tapping on the contact phone number).   
The logged user can change his information (both of the account and of the business card) by using the `SettingsActivity` and its fragments.
From here he can also change the appearence of the application, log out or delete his account. 

