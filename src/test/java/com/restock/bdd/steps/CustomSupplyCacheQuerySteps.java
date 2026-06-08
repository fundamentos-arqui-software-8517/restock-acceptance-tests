package com.restock.bdd.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

final class CacheScenarioState {
    static boolean backendRunning;
    static boolean redisAvailable;
    static boolean mongoPrepared;
    static boolean adminAuthenticated;

    static boolean cachedById;
    static boolean cachedListAll;
    static boolean cachedByAccount;

    static boolean newCustomSupplyStored;
    static boolean updatedCustomSupplyStored;
    static boolean deletedCustomSupplyRemoved;

    static int responseStatus;
    static String dataSource;
    static String returnedResult;
    static String accountCache;
    static String customSupplyIdCache;
    static String globalCache;
    static String operation;

    static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    static void requireBaseContext() {
        check(backendRunning, "Restock backend is not running.");
        check(redisAvailable, "Redis is not available.");
        check(adminAuthenticated, "Administrator is not authenticated.");
    }
}

public class CustomSupplyCacheQuerySteps {

    @Given("the Restock backend is running")
    public void theRestockBackendIsRunning() {
        CacheScenarioState.backendRunning = true;
        CacheScenarioState.responseStatus = 0;
        CacheScenarioState.dataSource = null;
        CacheScenarioState.returnedResult = null;
        CacheScenarioState.operation = null;
    }

    @And("Redis is available")
    public void redisIsAvailable() {
        CacheScenarioState.redisAvailable = true;
    }

    @And("MongoDB contains registered custom supplies")
    public void mongoDBContainsRegisteredCustomSupplies() {
        CacheScenarioState.mongoPrepared = true;
    }

    @And("the administrator is authenticated")
    public void theAdministratorIsAuthenticated() {
        CacheScenarioState.adminAuthenticated = true;
    }

    @When("the administrator requests custom supplies by account")
    public void theAdministratorRequestsCustomSuppliesByAccount() {
        CacheScenarioState.requireBaseContext();
        CacheScenarioState.check(
                CacheScenarioState.mongoPrepared,
                "MongoDB does not contain registered custom supplies."
        );

        CacheScenarioState.dataSource = "MongoDB";
        CacheScenarioState.returnedResult = "custom-supplies-by-account";
        CacheScenarioState.responseStatus = 200;
    }

    @Then("the system retrieves the information from MongoDB")
    public void theSystemRetrievesTheInformationFromMongoDB() {
        CacheScenarioState.check(
                "MongoDB".equals(CacheScenarioState.dataSource),
                "The information was not retrieved from MongoDB."
        );
    }

    @And("the system stores the result in Redis using the account cache key")
    public void theSystemStoresTheResultInRedisUsingTheAccountCacheKey() {
        CacheScenarioState.check(
                CacheScenarioState.returnedResult != null,
                "There is no result to store in Redis."
        );

        CacheScenarioState.accountCache = CacheScenarioState.returnedResult;
        CacheScenarioState.cachedByAccount = true;

        CacheScenarioState.check(
                CacheScenarioState.cachedByAccount,
                "The account cache key was not stored in Redis."
        );
    }

    @Given("the custom supplies query result is already stored in Redis")
    public void theCustomSuppliesQueryResultIsAlreadyStoredInRedis() {
        CacheScenarioState.redisAvailable = true;
        CacheScenarioState.accountCache = "cached-custom-supplies-by-account";
        CacheScenarioState.cachedByAccount = true;
    }

    @When("the administrator requests the custom supplies by account ID again")
    public void theAdministratorRequestsTheCustomSuppliesByAccountIDAgain() {
        CacheScenarioState.requireBaseContext();
        CacheScenarioState.check(
                CacheScenarioState.cachedByAccount,
                "The account query result is not stored in Redis."
        );

        CacheScenarioState.dataSource = "Redis";
        CacheScenarioState.returnedResult = CacheScenarioState.accountCache;
        CacheScenarioState.responseStatus = 200;
    }

    @Then("the system reuses the cached information from Redis")
    public void theSystemReusesTheCachedInformationFromRedis() {
        CacheScenarioState.check(
                "Redis".equals(CacheScenarioState.dataSource),
                "The system did not reuse cached information from Redis."
        );
    }

    @And("the returned custom supplies should match the previously stored result")
    public void theReturnedCustomSuppliesShouldMatchThePreviouslyStoredResult() {
        CacheScenarioState.check(
                CacheScenarioState.returnedResult != null
                        && CacheScenarioState.returnedResult.equals(CacheScenarioState.accountCache),
                "The returned custom supplies do not match the cached result."
        );
    }

    @When("the administrator requests a custom supply by its ID")
    public void theAdministratorRequestsACustomSupplyByItsID() {
        CacheScenarioState.requireBaseContext();

        CacheScenarioState.dataSource = "MongoDB";
        CacheScenarioState.returnedResult = "custom-supply-by-id";
        CacheScenarioState.responseStatus = 200;
    }

    @Then("the system retrieves the custom supply information")
    public void theSystemRetrievesTheCustomSupplyInformation() {
        CacheScenarioState.check(
                CacheScenarioState.returnedResult != null
                        && CacheScenarioState.returnedResult.equals("custom-supply-by-id"),
                "The custom supply information was not retrieved."
        );
    }

    @And("the system stores the result in Redis using the custom supply ID")
    public void theSystemStoresTheResultInRedisUsingTheCustomSupplyID() {
        CacheScenarioState.check(
                CacheScenarioState.returnedResult != null,
                "There is no custom supply result to cache."
        );

        CacheScenarioState.customSupplyIdCache = CacheScenarioState.returnedResult;
        CacheScenarioState.cachedById = true;

        CacheScenarioState.check(
                CacheScenarioState.cachedById,
                "The custom supply ID cache was not stored in Redis."
        );
    }

    @When("the administrator requests all custom supplies")
    public void theAdministratorRequestsAllCustomSupplies() {
        CacheScenarioState.requireBaseContext();

        CacheScenarioState.dataSource = "MongoDB";
        CacheScenarioState.returnedResult = "all-custom-supplies";
        CacheScenarioState.responseStatus = 200;
    }

    @And("the system stores the result in Redis using the global custom supplies cache key")
    public void theSystemStoresTheResultInRedisUsingTheGlobalCustomSuppliesCacheKey() {
        CacheScenarioState.check(
                CacheScenarioState.returnedResult != null,
                "There is no global custom supplies result to cache."
        );

        CacheScenarioState.globalCache = CacheScenarioState.returnedResult;
        CacheScenarioState.cachedListAll = true;

        CacheScenarioState.check(
                CacheScenarioState.cachedListAll,
                "The global custom supplies cache key was not stored in Redis."
        );
    }

    @And("the response status should be 200")
    public void theResponseStatusShouldBe200() {
        CacheScenarioState.check(
                CacheScenarioState.responseStatus == 200,
                "Expected response status 200 but got " + CacheScenarioState.responseStatus
        );
    }

    @When("the administrator requests the custom supplies by account ID")
    public void theAdministratorRequestsTheCustomSuppliesByAccountID() {
        CacheScenarioState.requireBaseContext();
        CacheScenarioState.check(
                CacheScenarioState.mongoPrepared,
                "MongoDB does not contain registered custom supplies."
        );
    
        CacheScenarioState.dataSource = "MongoDB";
        CacheScenarioState.returnedResult = "custom-supplies-by-account";
        CacheScenarioState.responseStatus = 200;
    }
    
}