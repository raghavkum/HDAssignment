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
package com.advanced.customerreview;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.customerreview.CustomerReviewService;


/**
 * Advanced version of the {@link CustomerReviewService}.
 */
public interface AdvancedCustomerReviewService extends CustomerReviewService
{
	/**
	 * Returns total number of customer reviews whose ratings are within a given range (inclusive) for a product.
	 *
	 * @param product
	 *           Product for which we need to find the reviews
	 * @param greaterThan
	 *           reviews greater than this number need to be found
	 * @param lessThan
	 *           reviews less than this number need to be found
	 * @return Integer the number of reviews for product within range
	 */
	int getReviewsForProductWithRange(final ProductModel product, final Double greaterThan, final Double lessThan);
}
