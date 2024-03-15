# Api Style Guide

There's a few conventions that are being followed by the API.

The API is loosely a resource oriented API. This means that the API is designed around resources, and the operations that can be performed on those resources.

Eg.

- `GET /tasks` - Get a list of tasks
- `POST /attempts` - Create a new attempt

## HTTP Methods

The currently allowed API methods are:

- `GET` - Retrieve a resource
- `POST` - Create a new resource
- `PUT` - Update a resource
- `DELETE` - Delete a resource

## Exceptions

There are a few exceptions to the resource oriented API:

- `GET /auth/<login/logout/callback>` - these are action oriented related to authentication
- `GET /user` - this is a special case where the user resource is the currently logged in user
