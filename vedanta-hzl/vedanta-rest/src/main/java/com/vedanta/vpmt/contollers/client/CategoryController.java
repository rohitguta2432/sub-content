package com.vedanta.vpmt.contollers.client;

import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.model.Category;
import com.vedanta.vpmt.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "category", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "{categoryId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Category>> getCategoryById(@PathVariable("categoryId") Long categoryId) {

		Category category = null;
		try {
			category = categoryService.get(categoryId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<Category>>(
				new Response<Category>(HttpStatus.OK.value(), "Category fetched successfully", category),
				HttpStatus.OK);
	}

	@RequestMapping(value = "business-unit-id/{businessUnitId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Category>>> getCategoriesByBusinessUnitId(
			@PathVariable("businessUnitId") Long businessUnitId) {
		return new ResponseEntity<Response<List<Category>>>(
				new Response<List<Category>>(HttpStatus.OK.value(), "Categories fetched successfully by business unit",
						categoryService.getCategoriesByBusinessUnitId(businessUnitId)),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "categories", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Category>>> getAllCategories() {

		List<Category> categories = new ArrayList<>();
		try {
			categories = categoryService.getAllCategories();
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<Category>>>(
				new Response<List<Category>>(HttpStatus.OK.value(), "Categories fetched successfully", categories),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Response<Category>> save(@RequestBody Category category) {

		try {
			category = categoryService.save(category);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<Category>>(
				new Response<Category>(HttpStatus.OK.value(), "Category saved successfully", category), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "update", method = RequestMethod.PUT)
	public ResponseEntity<Response<Category>> update(@RequestBody Category category) {

		try {
			category = categoryService.save(category);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<Category>>(
				new Response<Category>(HttpStatus.OK.value(), "Category saved successfully", category), HttpStatus.OK);
	}
}
