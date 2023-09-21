# DCSA-Edocumentation - Booking and EBL

## Developer on-boarding Building and Running the project

The recommended flow for setting up the development machine for the sake of modifying
the reference implementation is to:

1) Set up a GitHub Personal Access Token as mentioned [here](https://github.com/dcsaorg/DCSA-Core/blob/master/README.md#how-to-use-dcsa-core-packages).
2) Clone **DCSA-Edocumentation** (with ``--recurse-submodules`` option.)
3) Set up a database via: `docker compose up -d -V --build dcsa-test-db`
4) Build once using ``mvn package``
   - This step will hopefully be obsolete in the future.
5) Have IDEA run the `Application.class` as a spring boot application with the following
   profiles active: `dev,localdb,logsql,nosecurity`
   - If you have the community edition of IDEA, set the `SPRING_PROFILES_ACTIVE` environment
     variable to `dev,localdb,logsql,nosecurity`.
6) Verify if the application is running: `curl http://localhost:9090/actuator/health`
7) Run the test suite via `newman run postman_collection.json`

You will often need to reset your database, which can be done by:

 1) Stop the application
 2) Run `docker compose down`
 3) Run `docker compose up -d -V --build dcsa-test-db`
 4) Start the application again.

We do not offer migration from previous versions of the database.  Any change that changes the SQL
will require a reset of the database.

## Non-developer usage of the reference implementation

As a non-DCSA developer wanting to use / test the reference implementation, the recommended
flow is:

1) Set up a GitHub Personal Access Token as mentioned [here](https://github.com/dcsaorg/DCSA-Core/blob/master/README.md#how-to-use-dcsa-core-packages).
2) Clone **DCSA-Edocumentation** (with ``--recurse-submodules`` option.)
3) Build the application using ``mvn package``
4) Set up the reference implementation via: `docker compose up -d -V --build`
   - Note that `docker compose` by default does **not** load the test data (the sql files marked as test).
     For that, you will have to run the `docker compose` with the environment variable
     `SPRING_PROFILES_ACTIVE=nosecurity,loadtestdata` (`nosecurity` is the default if you do not set this
     variable). At the moment, this step is required if you want the postman collection tests to succeed.
5) Verify if the application is running: `curl http://localhost:9090/actuator/health`
6) **If** test data was loaded (see step 4), you can now run the postman tests with
   `newman run postman_collection.json`


You are also welcome to use the DCSA developer flow, which should also work for this purpose.

## Security considerations

This reference implementation does not do any authentication/authorization and should not be used
in production as is. Using this as is in production would expose data for all parties to all other
parties without checking whether they should have access.

## Testing

DCSA maintains a number of integration tests in the [postman_collection.json](postman_collection.json).
While they have dependencies on data loading, they may be useful as a starting point of other parties
that are implementing the standard.  The tests are expected to work out of the box on the reference
implementation itself.

The tests may be useful to others as a starting point, but you may need to edit the payloads to work with
your specific test data.

The tests can either be imported in to postman or run via newman `newman run postman_collection.json`.

## DEVELOPMENT FLOW

`master` is the main development branch.

`pre-release` and `release` are tagged and should be used as a stable version.

Development continues on `master` and feature branches are created based on `master`.

A typical development flow would look like:

1) Create a feature branch with `master` as base, proceed to make changes to feature branch.
2) Raise PR against `master`. When a PR is raised against master a CI is run to ensure everything is fine.
3) Merge with `master` after approval by at least one verified code owner and successful CI run.

> Note: If changes are required in the `DCSA-Shared-Kernel`, those changes should first be merged into the respective `master` branches before continuing development in this repository.

4) If development has been completed as per requirements for a given API version, `master` must be tagged to <br>
   create a `release` or `pre-release` accordingly.

When bug fixes are required or changes in pre-release versions are needed, we branch off using the respective <br>
tags and continue development on that branch. It is important to note that these changes must be cherry-picked <br>
and included into `master`.
