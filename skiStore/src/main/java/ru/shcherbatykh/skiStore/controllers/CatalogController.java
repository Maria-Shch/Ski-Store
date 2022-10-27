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
import ru.shcherbatykh.skiStore.models.User;
import ru.shcherbatykh.skiStore.models.Value;
import ru.shcherbatykh.skiStore.services.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    private final ModelOfInventoryService modelOfInventoryService;
    private final ModelTypeService modelTypeService;
    private final UserService userService;
    private final FiltrationService filtrationService;
    private final InventoryService inventoryService;
    private final CartService cartService;

    public CatalogController(ModelOfInventoryService modelOfInventoryService, ModelTypeService modelTypeService,
                             UserService userService, FiltrationService filtrationService,
                             InventoryService inventoryService, CartService cartService) {
        this.modelOfInventoryService = modelOfInventoryService;
        this.modelTypeService = modelTypeService;
        this.userService = userService;
        this.filtrationService = filtrationService;
        this.inventoryService = inventoryService;
        this.cartService = cartService;
    }

    @GetMapping
    public String getCatalogPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        model.addAttribute("modelTypes", modelTypeService.getModelTypes());
        return "catalog/catalog";
    }

    @GetMapping(value = {"/ski_poles", "/roller_skis", "/ski_boots", "/bindings", "/roller_ski_poles", "/ski"})
    public String getCategoryPage(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        fillingModelForCategoryPage(model, userDetails, Paths.get(request.getRequestURI()).getFileName().toString(), null);
        return "catalog/category";
    }

    @PostMapping(value = {"/ski_poles", "/roller_skis", "/ski_boots", "/bindings", "/roller_ski_poles", "/ski"})
    public String getCategoryPageAfterFilter(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails userDetails,
                                             @ModelAttribute("filter") Filter filter) {
        fillingModelForCategoryPage(model, userDetails, Paths.get(request.getRequestURI()).getFileName().toString(), filter);
        return "catalog/category";
    }

    private void fillingModelForCategoryPage(Model model, UserDetails userDetails, String modelTypeNameEnglish, Filter filter){
        ModelType modelType = modelTypeService.getModelTypeByNameEn(modelTypeNameEnglish);
        List<ModelOfInventory> models = modelOfInventoryService.getModelsByModelType(modelType);

        CategoryResponse categoryResponse = CategoryResponse.builder()
                .modelType(modelType)
                .modelsOfInventory(models)
                .build();

        if(filter == null){
            filter = Filter.builder()
                    .filtrationCategories(filtrationService.getFiltrationParams(modelType))
                    .build();
        }

        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        model.addAttribute("categoryResponse", categoryResponse);
        model.addAttribute("filter", filter);
    }

    @GetMapping(value = {"/ski_poles/{id}", "/roller_skis/{id}", "/ski_boots/{id}", "/bindings/{id}", "/roller_ski_poles/{id}", "/ski/{id}"})
    public String getModelPage(Model model, @AuthenticationPrincipal UserDetails userDetails,  @PathVariable long id) {
        fillingModelForModelPage(model, userDetails, id);
        return "catalog/model";
    }

    @PostMapping("/add/{modelId}")
    public String addInventoryToCart(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable long modelId,
                                     @ModelAttribute("selectedValue") Value selectedValue) {

        ModelOfInventory modelOfInventory = modelOfInventoryService.getModel(modelId);
        User user = userService.getUserByUserDetails(userDetails);
        cartService.addToCart(user, modelOfInventory, selectedValue);

        fillingModelForModelPage(model, userDetails, modelId);
        return String.format("redirect:/catalog/%s/%d", modelOfInventory.getModelType().getNameEnglish(), modelId);
    }

    private void fillingModelForModelPage(Model model, UserDetails userDetails, long idModel){
        ModelOfInventory modelOfInventory = modelOfInventoryService.getModel(idModel);
        DynamicInventoryAttribute dynamicInventoryAttributeByModel = inventoryService.getDynamicInventoryAttributeByModel(modelOfInventory);

        ModelOfInventoryResponse modelOfInventoryResponse = ModelOfInventoryResponse.builder()
                .modelOfInventory(modelOfInventory)
                .isPresentInStock(inventoryService.checkIsPresentInventoryInStockByModel(modelOfInventory))
                .isPresentDynamicInventoryAttribute(dynamicInventoryAttributeByModel != null)
                .dynamicInventoryAttribute(dynamicInventoryAttributeByModel)
                .build();

        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        model.addAttribute("modelOfInventoryResponse", modelOfInventoryResponse);
    }
}
