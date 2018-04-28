package com.vedanta.vpmt.job;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.google.gson.Gson;
import com.vedanta.vpmt.config.quartz.QuartzConfig;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.ScoreCardDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Field;
import com.vedanta.vpmt.model.FormSaved;
import com.vedanta.vpmt.model.Group;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCardData;
import com.vedanta.vpmt.model.ScoringCriteria;
import com.vedanta.vpmt.model.TemplateGroup;
import com.vedanta.vpmt.service.FormSaveService;
import com.vedanta.vpmt.service.ScoreCardService;
import com.vedanta.vpmt.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FormAggregation implements Job {

	@Value("${cron.expression.formAggregation}")
	private String cronExpression;
	
	@Autowired
	private FormSaveService formSaveService;
	
	@Autowired
	private ScoreCardService scoreCardService;
	
	@Autowired
	private ScoreCardDao scoreCardDao;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		this.populateScoreCard(1L);
	    //("Form Aggrigation stop..!");
		Map<Long, Map<Long,Long>> map = new HashMap<>();
		
		Map<Long,Long> templateMap = new HashMap<>();
		templateMap.put(3L, 4L);
		map.put(5L, templateMap);
		
	}
	
	private Double calculateScore(Double formAvg,List<ScoringCriteria> scoringCriterias){
		try{
			Double scoreValue = 0.0d;
			if(scoringCriterias.size() > 0){
				
				int checkOccurance = 0;
				for(ScoringCriteria scoringCriteria : scoringCriterias){
					
					if(scoringCriteria.getOperator().equals("ASSIGN")){
						scoreValue = formAvg;
						break;
					}
					
					if(scoringCriteria.getOperator().equals("LESS_THAN")){
						if(formAvg.intValue() < Integer.parseInt(scoringCriteria.getRightOperand())){
							scoreValue = Double.parseDouble(String.valueOf(scoringCriteria.getOutScore()));
							checkOccurance = 1;
						}else{
							if(checkOccurance == 1){
									break;
							}
						}
					}
					
					if(scoringCriteria.getOperator().equals("GREATER_THAN")){
						if(formAvg.intValue() > Integer.parseInt(scoringCriteria.getRightOperand())){
							scoreValue = Double.parseDouble(String.valueOf(scoringCriteria.getOutScore()));
							checkOccurance = 1;
						}
					}
					
					if(scoringCriteria.getOperator().equals("LESS_THAN_EQUAL")){
						if(formAvg.intValue() <= Integer.parseInt(scoringCriteria.getRightOperand())){
							scoreValue = Double.parseDouble(String.valueOf(scoringCriteria.getOutScore()));
							checkOccurance = 1;
						}else{
							if(checkOccurance == 1){
									break;
							}
						}
					}
					
					if(scoringCriteria.getOperator().equals("GREATER_THAN_EQUAL")){
						if(formAvg.intValue() > Integer.parseInt(scoringCriteria.getRightOperand())){
							scoreValue = Double.parseDouble(String.valueOf(scoringCriteria.getOutScore()));
							checkOccurance = 1;
						}
					}
					
					if(scoringCriteria.getOperator().equals("EQUALS")){
						if(formAvg.intValue() == Integer.parseInt(scoringCriteria.getRightOperand())){
							scoreValue = Double.parseDouble(String.valueOf(scoringCriteria.getOutScore()));
							checkOccurance = 1;
						}else{
							if(checkOccurance == 1){
									break;
							}
						}
					}
					
					if(scoringCriteria.getOperator().equals("NOT_EQUAL")){
						if(formAvg.intValue() != Integer.parseInt(scoringCriteria.getRightOperand())){
							scoreValue = Double.parseDouble(String.valueOf(scoringCriteria.getOutScore()));
							checkOccurance = 1;
						}else{
							if(checkOccurance == 1){
									break;
							}
						}
					}
					
				}
			}
			return scoreValue;
			
		}catch(VedantaException e){
			String msg = "Error while getting Contracts for User";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}
	
	private Map<String,Double> getWeigtageAndScoreValue(ScoreCard scoreCard, Long fieldId, Double formAvg){
		
		Map<String,Double> weigtageAndScoreValue = new HashMap<>();
		
		Map<String, Object> s = scoreCardService.getScoreCardTemplateMapByScoreCardIdOnFormAggrigation(scoreCard.getId());
		List<TemplateGroup> templateGroups = (List<TemplateGroup>) s.get(Constants.TEMPLATE_GROUPS);
		for(TemplateGroup templateGroup : templateGroups){
			Group groupDetails = templateGroup.getGroup();
			for(Field field : groupDetails.getFields()){
				if(field.getId() == fieldId){
					Double weightage = field.getWeight()/5;
					weigtageAndScoreValue.put("weightageValue", weightage);
					weigtageAndScoreValue.put("scoreValue", this.calculateScore(formAvg, field.getScoringCriteria()));
				}
			}
		}
		
		return weigtageAndScoreValue;
	}
	
	private void populateScoreCard(Long businessUnitId){
		
		//("Form Aggrigation start..!");
		Gson g = new Gson();
		Set<Long> formIds = new HashSet<Long>();
		formIds.add(5L);formIds.add(6L);formIds.add(7L);formIds.add(8L);
		formIds.add(9L);formIds.add(10L);formIds.add(11L);formIds.add(12L);formIds.add(13L);
		
		int year = DateUtil.getYear();
		int yearScoreCard = DateUtil.getYear();
		if(DateUtil.getMonthOfYear() == 0){
			yearScoreCard--;
		}
		int month = DateUtil.getMonthOfYear();
		int prevMonth = DateUtil.getPreviouseMonthOfYear();
		
		List<FormSaved> saveFormList = formSaveService.getAllFormSavedByFormIdAndMonthAndYearAndStatus(formIds, month, year, Constants.STATUS_SUBMITTED, businessUnitId);
		Boolean flag = false;
		//(saveFormList);
		for(FormSaved formSaved : saveFormList){
			
			/*
			 * Populate all those scorecard, who have templateId 3
			 *  and fieldId 4 for formId 5
			 *  and fieldId 9 for formId 6
			 */
			if(formSaved.getFormId() == 5L || formSaved.getFormId() == 6L){
				List<ScoreCard> scoreCards = scoreCardService.getAllScoreCardByTemplateIdAndMonthIdAndYearIdAndStatus(3L,prevMonth, yearScoreCard, Constants.STATUS_NOT_FILLED, businessUnitId);
				for(ScoreCard scoreCard : scoreCards){
					if(ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() <= 0){
						scoreCardService.firstScorecardEntry(scoreCard);
					}
					scoreCard = scoreCardService.get(scoreCard.getId()); 
					
					scoreCard.setScoreCardData(scoreCardService.getScoreCardDataFromScoreCard(scoreCard));
					
					if(formSaved.getFormId() == 5L){
						
						if(!ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() > 0){
							flag = true;
							for(ScoreCardData scorecardData : scoreCard.getScoreCardData()){
								if(scorecardData.getFieldId() == 4L){
									scorecardData.setIsAutoPopulated(1);
									scorecardData.setValue(String.valueOf(formSaved.getAverage()));
									Map<String,Double> weightageAndScoreValue = getWeigtageAndScoreValue(scoreCard,scorecardData.getFieldId(),formSaved.getAverage());
									scorecardData.setScore((Double)weightageAndScoreValue.get("scoreValue"));
									scorecardData.setWeightScore((Double)weightageAndScoreValue.get("weightageValue"));
								}
							}
						}
					}
					if(formSaved.getFormId() == 6L){
						if(!ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() > 0){
							flag = true;
							for(ScoreCardData scorecardData : scoreCard.getScoreCardData()){
								if(scorecardData.getFieldId() == 9L){
									scorecardData.setIsAutoPopulated(1);
									scorecardData.setValue(String.valueOf(formSaved.getAverage()));
									Map<String,Double> weightageAndScoreValue = getWeigtageAndScoreValue(scoreCard,scorecardData.getFieldId(),formSaved.getAverage());
									scorecardData.setScore((Double)weightageAndScoreValue.get("scoreValue"));
									scorecardData.setWeightScore((Double)weightageAndScoreValue.get("weightageValue"));
								}
							}
						}
					}
					if(flag){
						flag = false;
						scoreCard.setScorecardDataJson(g.toJson(scoreCard.getScoreCardData()));
						scoreCardDao.save(scoreCard);
						//("Populated ScoreCardId : "+scoreCard.getId());
					}
				}
			}
			
			/*
			 * Populate all those scorecard, who have templateId 5
			 *  and fieldId 28 for formId 7
			 *  and fieldId 27 for formId 8
			 */
			if(formSaved.getFormId() == 7L || formSaved.getFormId() == 8L){
				List<ScoreCard> scoreCards = scoreCardService.getAllScoreCardByTemplateIdAndMonthIdAndYearIdAndStatus(5L,prevMonth, yearScoreCard, Constants.STATUS_NOT_FILLED, businessUnitId);
				for(ScoreCard scoreCard : scoreCards){
					
					if(ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() <= 0){
						scoreCardService.firstScorecardEntry(scoreCard);
					}
					scoreCard = scoreCardService.get(scoreCard.getId());
					
					scoreCard.setScoreCardData(scoreCardService.getScoreCardDataFromScoreCard(scoreCard));
					if(formSaved.getFormId() == 7L){
						
						if(!ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() > 0){
							flag = true;
							for(ScoreCardData scorecardData : scoreCard.getScoreCardData()){
								if(scorecardData.getFieldId() == 28L){
									scorecardData.setIsAutoPopulated(1);
									scorecardData.setValue(String.valueOf(formSaved.getAverage()));
									Map<String,Double> weightageAndScoreValue = getWeigtageAndScoreValue(scoreCard,scorecardData.getFieldId(),formSaved.getAverage());
									scorecardData.setScore((Double)weightageAndScoreValue.get("scoreValue"));
									scorecardData.setWeightScore((Double)weightageAndScoreValue.get("weightageValue"));
								}
							}
						}
					}
					if(formSaved.getFormId() == 8L){
						
						if(!ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() > 0){
							flag = true;
							for(ScoreCardData scorecardData : scoreCard.getScoreCardData()){
								if(scorecardData.getFieldId() == 27L){
									scorecardData.setIsAutoPopulated(1);
									scorecardData.setValue(String.valueOf(formSaved.getAverage()));
									Map<String,Double> weightageAndScoreValue = getWeigtageAndScoreValue(scoreCard,scorecardData.getFieldId(),formSaved.getAverage());
									scorecardData.setScore((Double)weightageAndScoreValue.get("scoreValue"));
									scorecardData.setWeightScore((Double)weightageAndScoreValue.get("weightageValue"));
								}
							}
						}
					}
					if(flag){
						flag = false;
						scoreCard.setScorecardDataJson(g.toJson(scoreCard.getScoreCardData()));
						scoreCardDao.save(scoreCard);
						//("Populated ScoreCardId : "+scoreCard.getId());
					}
				}
			}
			
			/*
			 * Populate all those scorecard, who have templateId 2
			 *  and fieldId 24 for formId 9
			 */
			if(formSaved.getFormId() == 9L){
				List<ScoreCard> scoreCards = scoreCardService.getAllScoreCardByTemplateIdAndMonthIdAndYearIdAndStatus(2L,prevMonth, yearScoreCard, Constants.STATUS_NOT_FILLED, businessUnitId);
				for(ScoreCard scoreCard : scoreCards){
					if(ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() <= 0){
						scoreCardService.firstScorecardEntry(scoreCard);
					}
					scoreCard = scoreCardService.get(scoreCard.getId());
					
					scoreCard.setScoreCardData(scoreCardService.getScoreCardDataFromScoreCard(scoreCard));
					
					if(!ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() > 0){
						flag = true;
						for(ScoreCardData scorecardData : scoreCard.getScoreCardData()){
							if(scorecardData.getFieldId() == 24L){
								scorecardData.setIsAutoPopulated(1);
								scorecardData.setValue(String.valueOf(formSaved.getAverage()));
								Map<String,Double> weightageAndScoreValue = getWeigtageAndScoreValue(scoreCard,scorecardData.getFieldId(),formSaved.getAverage());
								scorecardData.setScore((Double)weightageAndScoreValue.get("scoreValue"));
								scorecardData.setWeightScore((Double)weightageAndScoreValue.get("weightageValue"));
							}
						}
					}
					if(flag){
						flag = false;
						scoreCard.setScorecardDataJson(g.toJson(scoreCard.getScoreCardData()));
						scoreCardDao.save(scoreCard);
						//("Populated ScoreCardId : "+scoreCard.getId());
					}
				}
			}
			
			/*
			 * Populate all those scorecard, who have templateId 1
			 *  and fieldId 18 for formId 10
			 *  and fieldId 19 for formId 11
			 */
			
			if(formSaved.getFormId() == 10L || formSaved.getFormId() == 11L){
				List<ScoreCard> scoreCards = scoreCardService.getAllScoreCardByTemplateIdAndMonthIdAndYearIdAndStatus(1L,prevMonth, yearScoreCard, Constants.STATUS_NOT_FILLED, businessUnitId);
				for(ScoreCard scoreCard : scoreCards){
					if(ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() <= 0){
						scoreCardService.firstScorecardEntry(scoreCard);
					}
					scoreCard = scoreCardService.get(scoreCard.getId());
					
					scoreCard.setScoreCardData(scoreCardService.getScoreCardDataFromScoreCard(scoreCard));
					if(formSaved.getFormId() == 10L){
						
						if(!ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() > 0){
							flag = true;
							for(ScoreCardData scorecardData : scoreCard.getScoreCardData()){
								if(scorecardData.getFieldId() == 18L){
									scorecardData.setIsAutoPopulated(1);
									scorecardData.setValue(String.valueOf(formSaved.getAverage()));
									Map<String,Double> weightageAndScoreValue = getWeigtageAndScoreValue(scoreCard,scorecardData.getFieldId(),formSaved.getAverage());
									scorecardData.setScore((Double)weightageAndScoreValue.get("scoreValue"));
									scorecardData.setWeightScore((Double)weightageAndScoreValue.get("weightageValue"));
								}
							}
						}
					}
					if(formSaved.getFormId() == 11L){
						
						if(!ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() > 0){
							flag = true;
							for(ScoreCardData scorecardData : scoreCard.getScoreCardData()){
								if(scorecardData.getFieldId() == 19L){
									scorecardData.setIsAutoPopulated(1);
									scorecardData.setValue(String.valueOf(formSaved.getAverage()));
									Map<String,Double> weightageAndScoreValue = getWeigtageAndScoreValue(scoreCard,scorecardData.getFieldId(),formSaved.getAverage());
									scorecardData.setScore((Double)weightageAndScoreValue.get("scoreValue"));
									scorecardData.setWeightScore((Double)weightageAndScoreValue.get("weightageValue"));
								}
							}
						}
					}
					if(flag){
						flag = false;
						scoreCard.setScorecardDataJson(g.toJson(scoreCard.getScoreCardData()));
						scoreCardDao.save(scoreCard);
						//("Populated ScoreCardId : "+scoreCard.getId());
					}
				}
			}
			
			/*
			 * Populate all those scorecard, who have templateId 4
			 *  and fieldId 12 for formId 12
			 *  and fieldId 13 for formId 13
			 */
			if(formSaved.getFormId() == 12L || formSaved.getFormId() == 13L){
				List<ScoreCard> scoreCards = scoreCardService.getAllScoreCardByTemplateIdAndMonthIdAndYearIdAndStatus(4L,prevMonth, yearScoreCard, Constants.STATUS_NOT_FILLED, businessUnitId);
				for(ScoreCard scoreCard : scoreCards){
					if(ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() <= 0){
						scoreCardService.firstScorecardEntry(scoreCard);
					}
					scoreCard = scoreCardService.get(scoreCard.getId());
					
					scoreCard.setScoreCardData(scoreCardService.getScoreCardDataFromScoreCard(scoreCard));
					if(formSaved.getFormId() == 12L){
						
						if(!ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() > 0){
							flag = true;
							for(ScoreCardData scorecardData : scoreCard.getScoreCardData()){
								if(scorecardData.getFieldId() == 12L){
									scorecardData.setIsAutoPopulated(1);
									scorecardData.setValue(String.valueOf(formSaved.getAverage()));
									Map<String,Double> weightageAndScoreValue = getWeigtageAndScoreValue(scoreCard,scorecardData.getFieldId(),formSaved.getAverage());
									scorecardData.setScore((Double)weightageAndScoreValue.get("scoreValue"));
									scorecardData.setWeightScore((Double)weightageAndScoreValue.get("weightageValue"));
								}
							}
						}
					}
					if(formSaved.getFormId() == 13L){
						
						if(!ObjectUtils.isEmpty(scoreCard.getScoreCardData()) && scoreCard.getScoreCardData().size() > 0){
							flag = true;
							for(ScoreCardData scorecardData : scoreCard.getScoreCardData()){
								if(scorecardData.getFieldId() == 13L){
									scorecardData.setIsAutoPopulated(1);
									scorecardData.setValue(String.valueOf(formSaved.getAverage()));
									Map<String,Double> weightageAndScoreValue = getWeigtageAndScoreValue(scoreCard,scorecardData.getFieldId(),formSaved.getAverage());
									scorecardData.setScore((Double)weightageAndScoreValue.get("scoreValue"));
									scorecardData.setWeightScore((Double)weightageAndScoreValue.get("weightageValue"));
								}
							}
						}
					}
					if(flag){
						flag = false;
						scoreCard.setScorecardDataJson(g.toJson(scoreCard.getScoreCardData()));
						scoreCardDao.save(scoreCard);
						//("Populated ScoreCardId : "+scoreCard.getId());
					}
				}
			}
		}
	}
	
	@Bean(name = "formAggregationJobBean")
	public JobDetailFactoryBean formAggregationJobBean() {
		return QuartzConfig.createJobDetail(this.getClass());
	}
	
	@Bean(name = "formAggregationJobBeanTrigger")
	public CronTriggerFactoryBean formAggregationJobBeanTrigger(
			@Qualifier("formAggregationJobBean") JobDetailFactoryBean fetchScheduledJobBean) {
		return QuartzConfig.createCronTrigger(fetchScheduledJobBean.getObject(), cronExpression);
	}
}
