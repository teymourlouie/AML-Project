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
* Matches Ontologies by finding literal full-name matches between their       *
* Lexicons and the Lexicon of a third mediating Ontology, by employing the    *
* LexicalMatcher.                                                             *
*                                                                             *
* @author Daniel Faria                                                        *
* @date 23-06-2014                                                            *
* @version 2.0                                                                *
******************************************************************************/
package aml.match;

import java.util.Set;

import aml.AML;
import aml.match.LexicalMatcher;
import aml.match.Matcher;
import aml.ontology.Lexicon;
import aml.ontology.Ontology;

public class MediatingMatcher implements Matcher
{

//Attributes

	//The external ontology used as a mediator
	private Lexicon ext;
	private String uri;
	//The type of lexical entry generated by this Lexicon extender
	private final String TYPE = "externalMatch";
	
//Constructors

	/**
	 * Constructs a MediatingMatcher with the given external Ontology
	 * @param x: the external Ontology
	 */
	public MediatingMatcher(Ontology x)
	{
		ext = x.getLexicon();
		uri = x.getURI();
	}

//Public Methods
	
	@Override
	public Alignment extendAlignment(Alignment a, double thresh)
	{
		AML aml = AML.getInstance();
		Lexicon source = aml.getSource().getLexicon();
		Lexicon target = aml.getTarget().getLexicon();
		LexicalMatcher lm = new LexicalMatcher();
		Alignment src = lm.match(source,ext,thresh);
		Alignment tgt = lm.match(target,ext,thresh);
		Alignment maps = new Alignment();
		for(Mapping m : src)
		{
			int sourceId = m.getSourceId();
			if(a.containsSource(sourceId))
				continue;
			int medId = m.getTargetId();
			Set<Integer> matches = tgt.getTargetMappings(medId);
			for(Integer j : matches)
			{
				if(a.containsTarget(j))
					continue;
				double similarity = Math.min(m.getSimilarity(),
						tgt.getSimilarity(j, medId));
				maps.add(new Mapping(sourceId,j,similarity));
			}
		}
		return maps;
	}
	
	/**
	 * Extends the Lexicons of the source and target Ontologies
	 * using WordNet
	 * @param thresh: the minimum confidence threshold below
	 * which synonyms will not be added to the Lexicons
	 */
	public void extendLexicons(double thresh)
	{
		AML aml = AML.getInstance();
		LexicalMatcher lm = new LexicalMatcher();
		Lexicon source = aml.getSource().getLexicon();
		Alignment src = lm.match(source,ext,thresh);
		for(Mapping m : src)
		{
			int t = m.getTargetId();
			Set<String> names = ext.getNames(t);
			for(String n : names)
			{
				double sim = m.getSimilarity() * ext.getWeight(n, t);
				if(sim >= thresh)
					source.add(m.getSourceId(), n, TYPE, uri, sim);
			}
		}
		Lexicon target = aml.getTarget().getLexicon();
		Alignment tgt = lm.match(target,ext,thresh);
		for(Mapping m : tgt)
		{
			int t = m.getTargetId();
			Set<String> names = ext.getNames(t);
			for(String n : names)
			{
				double sim = m.getSimilarity() * ext.getWeight(n, t);
				if(sim >= thresh)
					target.add(m.getSourceId(), n, TYPE, uri, sim);
			}
		}
	}
	
	@Override
	public Alignment match(double thresh)
	{
		AML aml = AML.getInstance();
		Lexicon source = aml.getSource().getLexicon();
		Lexicon target = aml.getTarget().getLexicon();
		LexicalMatcher lm = new LexicalMatcher();
		Alignment src = lm.match(source,ext,thresh);
		Alignment tgt = lm.match(target,ext,thresh);
		Alignment maps = new Alignment();
		for(Mapping m : src)
		{
			int sourceId = m.getSourceId();
			int medId = m.getTargetId();
			Set<Integer> matches = tgt.getTargetMappings(medId);
			for(Integer j : matches)
			{
				double similarity = Math.min(m.getSimilarity(),
						tgt.getSimilarity(j, medId));
				maps.add(new Mapping(sourceId,j,similarity));
			}
		}
		return maps;
	}
}
