# DCSA-Edocumentation - Booking and EBL

Building and Running the project,
-------------------------------------
**[RECOMMENDED]**
Setup a Github Personal Access Token as mentioned [here](https://github.com/dcsaorg/DCSA-Core/blob/master/README.md#how-to-use-dcsa-core-packages), then skip to **step 3**.

If you would like to build required DCSA packages individually, begin with step 1.

1) Clone **DCSA-Edocumentation** (with ``--recurse-submodules`` option.) and Build using, ``mvn package``

2) Initialize your local postgresql database as described in [datamodel/README.md](https://github.com/dcsaorg/DCSA-Information-Model/blob/master/README.md) \
   or If you have docker installed, you may skip this step and use the docker-compose command mentioned below to set it up (This will initialize the application along with the database).

3) Run application,
```
mvn spring-boot:run [options]

options:
 -Dspring-boot.run.arguments="--DB_HOSTNAME=localhost:5432 --LOG_LEVEL=DEBUG"
 ```

OR using **docker-compose**

```
docker-compose up -d -V --build
```

4) Verify if the application is running,
```
curl http://localhost:9090/v1/actuator/health
```

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
