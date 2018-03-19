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
package com.advanced.customerreview.dao.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.customerreview.dao.impl.DefaultCustomerReviewDao;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.advanced.customerreview.dao.AdvancedCustomerReviewDao;


/**
 * Implementation of {@link AdvancedCustomerReviewDao} and advancement of {@link DefaultCustomerReviewDao}.
 */
public class AdvancedCustomerReviewDaoImpl extends DefaultCustomerReviewDao implements AdvancedCustomerReviewDao
{

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(AdvancedCustomerReviewDaoImpl.class);

	@Override
	public int getReviewsForProductWithRange(final ProductModel product, final Double greaterThan, final Double lessThan)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.info("Searching for Product [" + product.getCode() + "] greaterThan [" + greaterThan + "] lessThan [" + lessThan);
		}

		final Map<String, Object> params = new HashMap<String, Object>();

		final StringBuilder queryBuilder = new StringBuilder(
				"SELECT COUNT({" + Item.PK + "}) FROM {" + "CustomerReview" + "} WHERE {" + "product" + "}=?product");
		params.put("product", product);

		if (greaterThan != null)
		{
			queryBuilder.append(" AND {rating}>=?greaterThan");
			params.put("greaterThan", greaterThan);
		}
		if (lessThan != null)
		{
			queryBuilder.append(" AND {rating}<=?lessThan");
			params.put("lessThan", lessThan);
		}

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString(), params);
		query.setResultClassList(Arrays.asList(Integer.class));
		final SearchResult<Integer> result = this.getFlexibleSearchService().search(query);

		int count = 0;
		if (result.getCount() == 1)
		{
			count = result.getResult().get(0).intValue();
		}
		if (LOG.isDebugEnabled())
		{
			LOG.info("Results: " + count);
		}

		return count;
	}
}
