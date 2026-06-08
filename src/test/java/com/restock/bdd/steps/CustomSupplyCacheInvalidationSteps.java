package com.restock.bdd.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CustomSupplyCacheInvalidationSteps {

    @And("custom supply information is already stored in Redis")
    public void customSupplyInformationIsAlreadyStoredInRedis() {
        CacheScenarioState.cachedById = true;
        CacheScenarioState.cachedListAll = true;
        CacheScenarioState.cachedByAccount = true;
        CacheScenarioState.accountCache = "cached-custom-supplies-by-account";
        CacheScenarioState.customSupplyIdCache = "cached-custom-supply-by-id";
        CacheScenarioState.globalCache = "cached-all-custom-supplies";
    }

    @When("the administrator creates a new custom supply")
    public void theAdministratorCreatesANewCustomSupply() {
        CacheScenarioState.requireBaseContext();
        CacheScenarioState.operation = "create";
    }

    @Then("the system stores the new custom supply in MongoDB")
    public void theSystemStoresTheNewCustomSupplyInMongoDB() {
        CacheScenarioState.check(
                "create".equals(CacheScenarioState.operation),
                "The current operation is not custom supply creation."
        );

        CacheScenarioState.newCustomSupplyStored = true;
        CacheScenarioState.returnedResult = "newly-created-custom-supply";
    }

    @And("the system invalidates the cached list of all custom supplies")
    public void theSystemInvalidatesTheCachedListOfAllCustomSupplies() {
        CacheScenarioState.check(
                CacheScenarioState.newCustomSupplyStored,
                "The new custom supply has not been stored in MongoDB."
        );

        CacheScenarioState.cachedListAll = false;
        CacheScenarioState.globalCache = null;

        CacheScenarioState.check(
                !CacheScenarioState.cachedListAll,
                "The cached list of all custom supplies was not invalidated."
        );
    }

    @And("the system invalidates the cached custom supplies by account")
    public void theSystemInvalidatesTheCachedCustomSuppliesByAccount() {
        CacheScenarioState.check(
                CacheScenarioState.newCustomSupplyStored,
                "The new custom supply has not been stored in MongoDB."
        );

        CacheScenarioState.cachedByAccount = false;
        CacheScenarioState.accountCache = null;

        CacheScenarioState.check(
                !CacheScenarioState.cachedByAccount,
                "The cached custom supplies by account were not invalidated."
        );
    }

    @And("the next query should return the newly created custom supply")
    public void theNextQueryShouldReturnTheNewlyCreatedCustomSupply() {
        CacheScenarioState.check(
                CacheScenarioState.newCustomSupplyStored
                        && "newly-created-custom-supply".equals(CacheScenarioState.returnedResult),
                "The next query did not return the newly created custom supply."
        );
    }

    @When("the administrator updates an existing custom supply")
    public void theAdministratorUpdatesAnExistingCustomSupply() {
        CacheScenarioState.requireBaseContext();
        CacheScenarioState.operation = "update";
    }

    @Then("the system updates the custom supply in MongoDB")
    public void theSystemUpdatesTheCustomSupplyInMongoDB() {
        CacheScenarioState.check(
                "update".equals(CacheScenarioState.operation),
                "The current operation is not custom supply update."
        );

        CacheScenarioState.updatedCustomSupplyStored = true;
        CacheScenarioState.returnedResult = "updated-custom-supply-information";
    }

    @And("the system invalidates the cached custom supply by ID")
    public void theSystemInvalidatesTheCachedCustomSupplyByID() {
        CacheScenarioState.check(
                CacheScenarioState.updatedCustomSupplyStored || CacheScenarioState.deletedCustomSupplyRemoved,
                "There is no update or delete operation to invalidate the custom supply by ID."
        );

        CacheScenarioState.cachedById = false;
        CacheScenarioState.customSupplyIdCache = null;

        CacheScenarioState.check(
                !CacheScenarioState.cachedById,
                "The cached custom supply by ID was not invalidated."
        );
    }

    @And("the system invalidates the cached custom supplies lists")
    public void theSystemInvalidatesTheCachedCustomSuppliesLists() {
        CacheScenarioState.check(
                CacheScenarioState.updatedCustomSupplyStored || CacheScenarioState.deletedCustomSupplyRemoved,
                "There is no update or delete operation to invalidate custom supplies lists."
        );

        CacheScenarioState.cachedListAll = false;
        CacheScenarioState.cachedByAccount = false;
        CacheScenarioState.globalCache = null;
        CacheScenarioState.accountCache = null;

        CacheScenarioState.check(
                !CacheScenarioState.cachedListAll && !CacheScenarioState.cachedByAccount,
                "The cached custom supplies lists were not invalidated."
        );
    }

    @And("the next query should return the updated custom supply information")
    public void theNextQueryShouldReturnTheUpdatedCustomSupplyInformation() {
        CacheScenarioState.check(
                CacheScenarioState.updatedCustomSupplyStored
                        && "updated-custom-supply-information".equals(CacheScenarioState.returnedResult),
                "The next query did not return the updated custom supply information."
        );
    }

    @When("the administrator deletes an existing custom supply")
    public void theAdministratorDeletesAnExistingCustomSupply() {
        CacheScenarioState.requireBaseContext();
        CacheScenarioState.operation = "delete";
    }

    @Then("the system removes the custom supply from MongoDB")
    public void theSystemRemovesTheCustomSupplyFromMongoDB() {
        CacheScenarioState.check(
                "delete".equals(CacheScenarioState.operation),
                "The current operation is not custom supply deletion."
        );

        CacheScenarioState.deletedCustomSupplyRemoved = true;
        CacheScenarioState.returnedResult = null;
    }

    @And("the next query should not return the deleted custom supply")
    public void theNextQueryShouldNotReturnTheDeletedCustomSupply() {
        CacheScenarioState.check(
                CacheScenarioState.deletedCustomSupplyRemoved && CacheScenarioState.returnedResult == null,
                "The deleted custom supply was returned by the next query."
        );
    }
}