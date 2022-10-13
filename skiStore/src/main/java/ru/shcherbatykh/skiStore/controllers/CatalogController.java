package ru.shcherbatykh.skiStore.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shcherbatykh.skiStore.classes.CategoryResponse;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.services.ModelOfInventoryService;
import ru.shcherbatykh.skiStore.services.ModelTypeService;
import ru.shcherbatykh.skiStore.services.UserService;

import java.util.List;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    private final ModelOfInventoryService modelOfInventoryService;
    private final ModelTypeService modelTypeService;
    private final UserService userService;

    public CatalogController(ModelOfInventoryService modelOfInventoryService, ModelTypeService modelTypeService, UserService userService) {
        this.modelOfInventoryService = modelOfInventoryService;
        this.modelTypeService = modelTypeService;
        this.userService = userService;
    }

    @GetMapping
    public String getCatalogPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        model.addAttribute("modelTypes", modelTypeService.getModelTypes());
        return "catalog/catalog";
    }

    @GetMapping(value = "/ski_poles")
    public String getSkiPolesPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        ModelType modelType = modelTypeService.getModelTypeByNameEn("ski_poles");
        List<ModelOfInventory> models = modelOfInventoryService.getModelByModelType(modelType);

        CategoryResponse categoryResponse = CategoryResponse.builder()
                .modelType(modelType)
                .modelsOfInventory(models)
                .build();

        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        model.addAttribute("categoryResponse", categoryResponse);
        return "catalog/category";
    }

    @GetMapping("/roller_skis")
    public String getRollerSkisPage(Model model) {
        return "catalog/catalog";
    }

    @GetMapping("/ski_boots")
    public String getSkiBootsPage( Model model) {
        return "catalog/catalog";
    }

    @GetMapping("/bindings")
    public String getBindingsPage( Model model) {
        return "catalog/catalog";
    }

    @GetMapping("/roller_ski_poles")
    public String getRollerSkiPolesPage( Model model) {
        return "catalog/catalog";
    }
}
