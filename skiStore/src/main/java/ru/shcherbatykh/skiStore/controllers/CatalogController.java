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

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Paths;
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

    @GetMapping(value = {"/ski_poles", "/roller_skis", "/ski_boots", "/bindings", "/roller_ski_poles", "/ski"})
    public String getCategoryPage(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        modelFilling(model, userDetails, Paths.get(request.getRequestURI()).getFileName().toString());
        return "catalog/category";
    }

    private Model modelFilling(Model model, UserDetails userDetails, String modelTypeNameEnglish){
        ModelType modelType = modelTypeService.getModelTypeByNameEn(modelTypeNameEnglish);
        List<ModelOfInventory> models = modelOfInventoryService.getModelByModelType(modelType);

        CategoryResponse categoryResponse = CategoryResponse.builder()
                .modelType(modelType)
                .modelsOfInventory(models)
                .build();

        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        model.addAttribute("categoryResponse", categoryResponse);
        return model;
    }
}
