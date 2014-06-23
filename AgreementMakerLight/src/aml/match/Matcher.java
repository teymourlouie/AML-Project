/******************************************************************************
* Copyright 2013-2014 LASIGE                                                  *
*                                                                             *
* Licensed under the Apache License, Version 2.0 (the "License"); you may     *
* not use this file except in compliance with the License. You may obtain a   *
* copy of the License at http://www.apache.org/licenses/LICENSE-2.0           *
*                                                                             *
* Unless required by applicable law or agreed to in writing, software         *
* distributed under the License is distributed on an "AS IS" BASIS,           *
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    *
* See the License for the specific language governing permissions and         *
* limitations under the License.                                              *
*                                                                             *
*******************************************************************************
* A matching algorithm that maps the source and target Ontologies, returning  *
* an Alignment between them, or extends a previous Alignment, returning only  *
* new Mappings.                                                               *
*                                                                             *
* @author Daniel Faria                                                        *
* @date 23-06-2013                                                            *
* @version 2.0                                                                *
******************************************************************************/
package aml.match;

public interface Matcher
{
	/**
	 * Extends the given Alignment between the source and target Ontologies
	 * @param a: the existing alignment
	 * @param thresh: the similarity threshold for the extention
	 * @return the alignment with the new mappings between the Ontologies
	 */
	public Alignment extendAlignment(Alignment a, double thresh);
	
	/**
	 * Matches the source and target Ontologies, returning an Alignment between them
	 * @param source: the source Ontology
	 * @param target: the target Ontology
	 * @param thresh: the similarity threshold for the alignment
	 * @return the alignment between the source and target ontologies
	 */
	public Alignment match(double thresh);
}
