/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2013, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.jpa.graph.internal.advisor;

import org.hibernate.LockMode;
import org.hibernate.engine.FetchStrategy;
import org.hibernate.engine.FetchStyle;
import org.hibernate.engine.FetchTiming;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.jpa.graph.spi.AttributeNodeImplementor;
import org.hibernate.loader.plan.spi.CollectionFetch;
import org.hibernate.loader.plan.spi.CompositeFetch;
import org.hibernate.loader.plan.spi.EntityFetch;
import org.hibernate.loader.plan.spi.Fetch;
import org.hibernate.loader.plan.spi.FetchOwner;

/**
 * @author Steve Ebersole
 */
public class AdviceHelper {
	private AdviceHelper() {
	}

	static Fetch buildFetch(FetchOwner fetchOwner, AttributeNodeImplementor attributeNode) {
		if ( attributeNode.getAttribute().isAssociation() ) {
			if ( attributeNode.getAttribute().isCollection() ) {
				return new CollectionFetch(
						(SessionFactoryImplementor) attributeNode.entityManagerFactory().getSessionFactory(),
						LockMode.NONE,
						fetchOwner,
						new FetchStrategy( FetchTiming.IMMEDIATE, FetchStyle.SELECT ),
						attributeNode.getAttributeName()
				);
			}
			else {
				return new EntityFetch(
						(SessionFactoryImplementor) attributeNode.entityManagerFactory().getSessionFactory(),
						LockMode.NONE,
						fetchOwner,
						attributeNode.getAttributeName(),
						new FetchStrategy( FetchTiming.IMMEDIATE, FetchStyle.SELECT )
				);
			}
		}
		else {
			return new CompositeFetch(
					(SessionFactoryImplementor) attributeNode.entityManagerFactory().getSessionFactory(),
					fetchOwner,
					attributeNode.getAttributeName()
			);
		}
	}
}
