package ru.shcherbatykh.skiStore.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.shcherbatykh.skiStore.classes.CategoryResponse;
import ru.shcherbatykh.skiStore.classes.DynamicInventoryAttribute;
import ru.shcherbatykh.skiStore.classes.Filter;
import ru.shcherbatykh.skiStore.classes.ModelOfInventoryResponse;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.services.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    private final ModelOfInventoryService modelOfInventoryService;
    private final ModelTypeService modelTypeService;
    private final SpecificationsService specificationsService;
    private final UserService userService;
    private final FiltrationService filtrationService;
    private final InventoryService inventoryService;

    public CatalogController(ModelOfInventoryService modelOfInventoryService, ModelTypeService modelTypeService,
                             SpecificationsService specificationsService, UserService userService, FiltrationService filtrationService, InventoryService inventoryService) {
        this.modelOfInventoryService = modelOfInventoryService;
        this.modelTypeService = modelTypeService;
        this.specificationsService = specificationsService;
        this.userService = userService;
        this.filtrationService = filtrationService;
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public String getCatalogPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        model.addAttribute("modelTypes", modelTypeService.getModelTypes());
        return "catalog/catalog";
    }

    @GetMapping(value = {"/ski_poles", "/roller_skis", "/ski_boots", "/bindings", "/roller_ski_poles", "/ski"})
    public String getCategoryPage(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        modelFilling(model, userDetails, Paths.get(request.getRequestURI()).getFileName().toString());
        return "catalog/category";
    }

    @PostMapping(value = {"/ski_poles", "/roller_skis", "/ski_boots", "/bindings", "/roller_ski_poles", "/ski"})
    public String getCategoryPageAfterFilter(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails userDetails,
                                             @ModelAttribute("filter") Filter filter) {

        modelFilling(model, userDetails, Paths.get(request.getRequestURI()).getFileName().toString(), filter);
        return "catalog/category";
    }

    private Model modelFilling(Model model, UserDetails userDetails, String modelTypeNameEnglish){
        ModelType modelType = modelTypeService.getModelTypeByNameEn(modelTypeNameEnglish);
        List<ModelOfInventory> models = modelOfInventoryService.getModelsByModelType(modelType);

        CategoryResponse categoryResponse = CategoryResponse.builder()
                .modelType(modelType)
                .modelsOfInventory(models)
                .build();

        Filter filter = Filter.builder()
                .filtrationCategories(filtrationService.getFiltrationParams(modelType))
                .build();

        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        model.addAttribute("categoryResponse", categoryResponse);
        model.addAttribute("filter", filter);
        return model;
    }

    private Model modelFilling(Model model, UserDetails userDetails, String modelTypeNameEnglish, Filter filter){
        modelFilling(model, userDetails, modelTypeNameEnglish);
        model.addAttribute("filter", filter);
        return model;
    }

    @GetMapping(value = {"/ski_poles/{id}", "/roller_skis/{id}", "/ski_boots/{id}", "/bindings/{id}", "/roller_ski_poles/{id}", "/ski/{id}"})
    public String getModelPage( Model model, @AuthenticationPrincipal UserDetails userDetails,  @PathVariable long id) {

        ModelOfInventory model1 = modelOfInventoryService.getModel(id);
        DynamicInventoryAttribute dynamicInventoryAttributeByModel = inventoryService.getDynamicInventoryAttributeByModel(model1);

        ModelOfInventoryResponse modelOfInventoryResponse = ModelOfInventoryResponse.builder()
                        .modelOfInventory(model1)
                        .isPresentInStock(inventoryService.checkIsPresentInventoryInStockByModel(model1))
                        .isPresentDynamicInventoryAttribute(dynamicInventoryAttributeByModel != null)
                        .dynamicInventoryAttribute(dynamicInventoryAttributeByModel)
                        .build();

        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        model.addAttribute("modelOfInventoryResponse", modelOfInventoryResponse);
        return "catalog/model";
    }
}
