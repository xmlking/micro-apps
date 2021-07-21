# API Test

## Test REST services

We are using [httpie](https://httpie.io/) CLI for REST testing

```bash
# list
http :8080/account

# get by Id
http :8080/account/67531e6d-fe00-4d78-afa1-ac008ced47af

# create
http POST :8080/account << END 
{
    "name": {
      "first": "sumo",
      "last": "demo"
    },
    "addresses": [
      {
        "suite": "A212",
        "street": "FourWinds Dr",
        "city": "Corona",
        "state": "CA",
        "code": "34453",
        "country": "USA",
        "location": [-77.0364, -38.8951]
      },
      {
        "suite": "B212",
        "street": "ThreeWinds Dr",
        "city": "Corona",
        "state": "CA",
        "code": "44553",
        "country": "USA",
        "location": [-77.0364, -38.8951]
      }
    ],
    "gender": "MALE",
    "age": 34,
    "email": "sumo@demo.com",
    "phone": "3334442222"
  }
END
# create person2
http POST :8080/account << END 
{
    "name": {
      "first": "sumo2",
      "last": "demo2"
    },
    "addresses": [
      {
        "suite": "A212",
        "street": "FourWinds Dr",
        "city": "Corona",
        "state": "CA",
        "code": "34453",
        "country": "USA",
        "location": [-77.0364, -38.8951]
      }
    ],
    "gender": "MALE",
    "age": 34,
    "email": "sumo2@demo.com",
    "phone": "2222222222"
  }
END

# add person2's address to person1
# http PATCH :8080/account/<addressId>/link/<personId>
http PATCH :8080/account/de85dd63-3a82-47c5-bcb9-0e8151a9dd0d/link/ac0337ef-33be-4559-9aea-eb56c67eaae1

# bad create request
http POST :8080/account << END 
{
    "name": {
      "first": "sumo😀"
      "last": ""
    },
    "addresses": [
      {
        "suite": "A212",
        "street": "FourWinds Dr",
        "city": "Corona",
        "state": "CA",
        "code": "22",
        "country": "USA",
        "location": [-77.0364, -38.8951]
      }
    ],
    "gender": "MALE",
    "age": 11,
    "email": "sumo@demo.com",
    "phone": "3334442222"
  }
END
# Update by Id
http PUT :8080/account/f5506576-7eca-4bd1-adb4-744b4981515b << END 
{
    "name": {
      "first": "sumo33",
      "last": "demo3"
    },
    "addresses": [
      {
        "suite": "C212",
        "street": "FourWinds Dr",
        "city": "Corona",
        "state": "CA",
        "code": "33333",
        "country": "USA",
        "location": [-77.0364, -38.8951]
      }
    ],
    "gender": "MALE",
    "age": 31,
    "email": "sumo3@demo.com",
    "phone": "3333333333"
  }
END

# Delete by Id
http DELETE :8080/account/ff44f30c-a042-4ba9-bc91-ebd9679b54dc

```
