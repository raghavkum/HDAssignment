/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.advanced.customerreview.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.impl.DefaultCustomerReviewService;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.advanced.customerreview.AdvancedCustomerReviewService;
import com.advanced.customerreview.dao.AdvancedCustomerReviewDao;


/**
 * Implementation of {@link AdvancedCustomerReviewService} and advancement of {@link DefaultCustomerReviewService}.
 */
public class AdvancedCustomerReviewServiceImpl extends DefaultCustomerReviewService implements AdvancedCustomerReviewService
{

	/** The advanced customer review dao. */
	@Autowired
	AdvancedCustomerReviewDao advancedCustomerReviewDao;

	/** The configuration service. */
	@Autowired
	ConfigurationService configurationService;

	/** The Constant SEMICOLON_CHAR. */
	private static final String SEMICOLON_CHAR = ";";

	@Override
	public int getReviewsForProductWithRange(final ProductModel product, final Double greaterThan, final Double lessThan)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("product", product);
		return advancedCustomerReviewDao.getReviewsForProductWithRange(product, greaterThan, lessThan);
	}

	/**
	 * Creates a Customer Review post validation.
	 *
	 * @param rating
	 *           rating given as part of the review
	 * @param headline
	 *           headline of the review
	 * @param comment
	 *           comment of the review
	 * @param user
	 *           User who has given the review
	 * @param product
	 *           Product for which review is given
	 * @return CustomerReviewModel the newly created customer review model
	 * @throws IllegalArgumentException
	 *            if data validation fails, a) Curse Word found b) Rating < 0
	 */
	@Override
	public CustomerReviewModel createCustomerReview(final Double rating, final String headline, final String comment,
			final UserModel user, final ProductModel product)
	{
		// Fetch curse words list from properties file
		final String listOfCurseWords = configurationService.getConfiguration().getString("customer.review.curse.words");

		// Validate curse words if comment and curse words both are non blank
		if (StringUtils.isNotBlank(listOfCurseWords) && StringUtils.isNotBlank(comment))
		{
			// Get list of comma separated curse words in comment
			final String curseWordsFound = Arrays.asList(listOfCurseWords.split(SEMICOLON_CHAR)).stream()
					.filter(curseWord -> StringUtils.containsIgnoreCase(comment, curseWord)).collect(Collectors.joining(", "));

			// If curse word found, throw an exception
			if (StringUtils.isNotBlank(curseWordsFound))
			{
				throw new IllegalArgumentException(
						String.format("Following curse words found in the comment - [%s].", curseWordsFound));
			}
		}

		// Rating cannot be less than 0
		if (rating != null && rating.doubleValue() < 0)
		{
			throw new IllegalArgumentException("Review Rating is less than zero");
		}

		// No validation error found in review comment
		return super.createCustomerReview(rating, headline, comment, user, product);
	}
}
