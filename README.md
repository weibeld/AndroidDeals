# Deals Hunt You

**Deals Hunt You** is an Android app that allows vendors to advertise discount coupons to potential customers. Interested customers see a list of these coupons and can grab and redeem them.


## User Stories

### Core

- Business users can create a discount coupon with a set of constraints (number of coupons, time period of redemption, etc.) and publish it for a price
- Customer users can see a tailored list of currently available coupons (matching their context, e.g. interests, location, etc.)
- Customer users can grab a coupon, which obliges them to either redeem the coupon within the defined constraints (e.g. validity period), or, if they miss to do so, to pay the value of the coupon to the vendor
- Payments between customer users and business users can be done either outside of the app (e.g. in cash), or as in-app mobile payments (e.g. PayPal, credit card), except for the case that a customer users fails to redeem a coupon, in which case the value of the coupon is transferred automatically from the customer to the vendor by mobile payment. This requires all users to define a mobile payment method on sign-up.

### Additional

- Business users can promote their coupons for an extra charge in different ways:
    - Appear at the top of the list
    - Delivered as a push notification to a subset of users. e.g. being within a certain geographical area (geofencing)
- Customer users can rate and review business users
- Business users can rate and review customer users
- Customer users can subscribe to business users to receive their offers in a prominent way (e.g. by push notifications)
- Business users can see their sales profile showing the income from all the coupons and their expenses for publishing the coupons
- Customer users can see their buying profile showing all the coupons they bought and how much money they saved with respect to the regular price (the vendor is required to provide the regular price when creating a coupon)


## Video Walkthrough

Here's a walkthrough of implemented user stories:

![Walkthrough](assets/walkthrough.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).


## Notes


## Open-source libraries used

- [Gson](https://github.com/google/gson) - Java serialization/deserialization library for converting JSON objects to Java objects and vice versa
- [DBFlow](https://github.com/Raizlabs/DBFlow) - Object-relational mapper (ORM) for Android
- [Retrolambda](https://github.com/evant/gradle-retrolambda) - Enable lambda expressions and method references for Java < 8
- [Stetho](http://facebook.github.io/stetho/) - Chrome-based Android debugging bridge
- [EventBus](http://greenrobot.org/eventbus/) - Pass events between Android application components


## License

    Copyright 2017 Hùng Nguyễn, Phong Nguyen Thanh, Daniel Weibel

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.