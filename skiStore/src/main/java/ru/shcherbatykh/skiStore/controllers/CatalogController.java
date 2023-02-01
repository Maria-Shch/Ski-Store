package ru.shcherbatykh.skiStore.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.shcherbatykh.skiStore.classes.*;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.models.User;
import ru.shcherbatykh.skiStore.models.Value;
import ru.shcherbatykh.skiStore.services.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    private final ModelOfInventoryService modelOfInventoryService;
    private final ModelTypeService modelTypeService;
    private final UserService userService;
    private final FiltrationService filtrationService;
    private final InventoryService inventoryService;
    private final CartService cartService;
    private final BrandService brandService;
    private final SeasonService seasonService;
    private final YearService yearService;
    private final AttributeService attributeService;

    public CatalogController(ModelOfInventoryService modelOfInventoryService, ModelTypeService modelTypeService,
                             UserService userService, FiltrationService filtrationService,
                             InventoryService inventoryService, CartService cartService, BrandService brandService,
                             SeasonService seasonService, YearService yearService, AttributeService attributeService) {
        this.modelOfInventoryService = modelOfInventoryService;
        this.modelTypeService = modelTypeService;
        this.userService = userService;
        this.filtrationService = filtrationService;
        this.inventoryService = inventoryService;
        this.cartService = cartService;
        this.brandService = brandService;
        this.seasonService = seasonService;
        this.yearService = yearService;
        this.attributeService = attributeService;
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
        List<ModelOfInventory> models;
        ModelType modelType = modelTypeService.getModelTypeByNameEn(modelTypeNameEnglish);

        if (filter != null) models = filtrationService.filtration(modelType, filter);
        else models = modelOfInventoryService.getModelsByModelType(modelType);

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
        DynamicInventoryAttribute DIA = inventoryService.getDynamicInventoryAttributeByModel(modelOfInventory);

        ModelOfInventoryResponse modelOfInventoryResponse = ModelOfInventoryResponse.builder()
                .modelOfInventory(modelOfInventory)
                .isPresentInStock(inventoryService.checkIsPresentInventoryInStockByModel(modelOfInventory))
                .isPresentDynamicInventoryAttribute(attributeService.isPresentDynamicInventoryAttribute(modelOfInventory.getModelType()))
                .dynamicInventoryAttribute(DIA)
                .newPrice(String.valueOf(modelOfInventory.getPrice()))  // The old values of a price and a discount are passed through
                .newDiscount(modelOfInventory.getDiscount())            // the ModelOfInventoryResponse for displaying current values in the inputs
                .build();

        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        model.addAttribute("modelOfInventoryResponse", modelOfInventoryResponse);

        if(userService.getRoleByUserDetails(userDetails).equals(Role.ADMIN)){
            if(attributeService.isPresentDynamicInventoryAttribute(modelOfInventory.getModelType())){
                AdminDynamicInventoryAttribute adminDIA = inventoryService.getAdminDynamicInventoryAttributeByModel(modelOfInventory);
                model.addAttribute("adminDynamicInventoryAttribute", adminDIA);
            }
            else {
                model.addAttribute("simpleModelQuantity", modelOfInventory.getInventories().get(0).getQuantity());
            }
        }
    }

    @PostMapping("/update/{modelId}/{field}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updatePriceOrDiscount(Model model, @AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable String field, @PathVariable long modelId,
                                        @ModelAttribute("newPrice") String newPrice, @ModelAttribute("newDiscount") String newDiscount) {
        ModelOfInventory modelOfInventory = modelOfInventoryService.getModel(modelId);
        if (Objects.equals(field, "price")) modelOfInventoryService.updatePrice(modelId, newPrice);
        else modelOfInventoryService.updateDiscount(modelId, newDiscount);
        fillingModelForModelPage(model, userDetails, modelId);
        return String.format("redirect:/catalog/%s/%d", modelOfInventory.getModelType().getNameEnglish(), modelId);
    }

    @PostMapping("/update/{modelId}/quantity")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateQuantity(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable long modelId,
                                        @ModelAttribute("adminDynamicInventoryAttribute") AdminDynamicInventoryAttribute adminDIA,
                                        @RequestParam(required = false) Integer simpleModelQuantity) {
        ModelOfInventory modelOfInventory = modelOfInventoryService.getModel(modelId);
        if(adminDIA.getValues() == null){
            inventoryService.updateQuantity(modelOfInventory, simpleModelQuantity);
        } else inventoryService.updateQuantity(modelOfInventory, adminDIA);

        fillingModelForModelPage(model, userDetails, modelId);
        return String.format("redirect:/catalog/%s/%d", modelOfInventory.getModelType().getNameEnglish(), modelId);
    }

    @GetMapping("/new/{modelTypeEn}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addNewModel (Model model, @AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable String modelTypeEn) {
        fillingModelForCreatingNewModelPage(model, userDetails, modelTypeEn);
        return "catalog/creatingModel";
    }

    @PostMapping("/new/{modelTypeEn}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addNewModel(Model model, @AuthenticationPrincipal UserDetails userDetails,
                              @ModelAttribute("modelCreator") ModelCreator modelCreator,
                              BindingResult bindingResult) {
        ModelOfInventory newModel = modelOfInventoryService.createNewModelOfInventory(modelCreator, bindingResult);

        if (bindingResult.hasErrors()) {
            if(modelCreator.getAdapterDynamicAttribute()!=null){
                modelCreator.refreshAdapterDynamicAttribute();
            }
            model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
            model.addAttribute("modelCreator", modelCreator);
            return "catalog/creatingModel";
        }

        return String.format("redirect:/catalog/%s/%d", modelCreator.getModelType().getNameEnglish(), newModel.getId());
    }


    private void fillingModelForCreatingNewModelPage(Model model, UserDetails userDetails, String modelTypeNameEn) {
        ModelType modelType = modelTypeService.getModelTypeByNameEn(modelTypeNameEn);

        ModelCreator modelCreator = ModelCreator.builder()
                .modelType(modelType)
                .brands(brandService.findAll())
                .seasons(seasonService.findAll())
                .years(yearService.findAll())
                .adaptersStaticAttributes(attributeService.getAdaptersStaticAttributeByModelType(modelType))
                .adapterDynamicAttribute(attributeService.getAdapterDynamicAttributeByModelType(modelType))
                .build();
        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        model.addAttribute("modelCreator", modelCreator);
    }
}
