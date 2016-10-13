--
--   Copyright 2014-2016 PetaByte Research Ltd.
--
--   Licensed under the Apache License, Version 2.0 (the "License");
--   you may not use this file except in compliance with the License.
--   You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--   Unless required by applicable law or agreed to in writing, software
--   distributed under the License is distributed on an "AS IS" BASIS,
--   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--   See the License for the specific language governing permissions and
--   limitations under the License.
--

-- MySQL dump 10.13  Distrib 5.5.43, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: redflags
-- ------------------------------------------------------
-- Server version	5.5.43-0+deb7u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `persistent_logins`
--

DROP TABLE IF EXISTS `persistent_logins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_address`
--

DROP TABLE IF EXISTS `te_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_address` (
  `id` varchar(200) NOT NULL,
  `buyerProfileUrl` varchar(200) DEFAULT NULL,
  `city` varchar(200) DEFAULT NULL,
  `contactPerson` varchar(200) DEFAULT NULL,
  `contactPoint` varchar(200) DEFAULT NULL,
  `country` varchar(200) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `fax` varchar(200) DEFAULT NULL,
  `infoUrl` varchar(200) DEFAULT NULL,
  `phone` varchar(200) DEFAULT NULL,
  `_raw` longtext,
  `street` varchar(200) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `zip` varchar(200) DEFAULT NULL,
  `organizationId` varchar(200) DEFAULT NULL,
  `rev` decimal(22,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_award`
--

DROP TABLE IF EXISTS `te_award`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_award` (
  `id` varchar(200) NOT NULL,
  `decisionDate` datetime DEFAULT NULL,
  `lotNumber` decimal(22,0) DEFAULT NULL,
  `lotTitle` longtext,
  `number` decimal(22,0) DEFAULT NULL,
  `numberOfOffers` decimal(22,0) DEFAULT NULL,
  `rawDecisionDate` varchar(200) DEFAULT NULL,
  `rawHeader` longtext,
  `rawLotNumber` varchar(200) DEFAULT NULL,
  `rawNumber` varchar(200) DEFAULT NULL,
  `rawNumberOfOffers` varchar(200) DEFAULT NULL,
  `rawTotalEstimatedValue` varchar(200) DEFAULT NULL,
  `rawTotalEstimatedValueVat` varchar(200) DEFAULT NULL,
  `rawTotalFinalValue` varchar(200) DEFAULT NULL,
  `rawTotalFinalValueVat` varchar(200) DEFAULT NULL,
  `subcontracting` longtext,
  `subcontractingRate` decimal(22,2) DEFAULT NULL,
  `totalEstimatedValue` decimal(22,0) DEFAULT NULL,
  `totalEstimatedValueCurr` varchar(200) DEFAULT NULL,
  `totalEstimatedValueVat` decimal(22,2) DEFAULT NULL,
  `totalFinalValue` decimal(22,0) DEFAULT NULL,
  `totalFinalValueCurr` varchar(200) DEFAULT NULL,
  `totalFinalValueVat` decimal(22,2) DEFAULT NULL,
  `noticeId` varchar(200) DEFAULT NULL,
  `rev` decimal(22,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_complementaryinfo`
--

DROP TABLE IF EXISTS `te_complementaryinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_complementaryinfo` (
  `id` varchar(200) NOT NULL,
  `additionalInfo` longtext,
  `genRegFWInfo` longtext,
  `lodgingOfAppeals` longtext,
  `rawRelToEUProjects` longtext,
  `recurrence` longtext,
  `refsToEUProjects` longtext,
  `relToEUProjects` decimal(1,0) DEFAULT NULL,
  `noticeId` longtext,
  `rev` decimal(22,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_contractingauthority`
--

DROP TABLE IF EXISTS `te_contractingauthority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_contractingauthority` (
  `id` varchar(200) NOT NULL,
  `purchasingOnBehalfOfOther` decimal(1,0) DEFAULT NULL,
  `rawPurchasingOnBehalfOfOther` longtext,
  `rev` decimal(22,0) DEFAULT NULL,
  `noticeId` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_cpv`
--

DROP TABLE IF EXISTS `te_cpv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_cpv` (
  `id` varchar(200) NOT NULL,
  `code` decimal(22,0) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `rev` decimal(22,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_data`
--

DROP TABLE IF EXISTS `te_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_data` (
  `id` varchar(200) NOT NULL,
  `country` varchar(200) DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `deadlineForDocs` datetime DEFAULT NULL,
  `directive` varchar(200) DEFAULT NULL,
  `documentSent` datetime DEFAULT NULL,
  `internetAddress` varchar(200) DEFAULT NULL,
  `oj` varchar(200) DEFAULT NULL,
  `originalLanguage` varchar(200) DEFAULT NULL,
  `place` varchar(200) DEFAULT NULL,
  `publicationDate` datetime DEFAULT NULL,
  `title` varchar(1024) DEFAULT NULL,
  `noticeId` varchar(200) DEFAULT NULL,
  `rev` decimal(22,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_datatype`
--

DROP TABLE IF EXISTS `te_datatype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_datatype` (
  `id` varchar(200) NOT NULL,
  `typeId` varchar(200) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `rev` decimal(22,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_duration`
--

DROP TABLE IF EXISTS `te_duration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_duration` (
  `id` varchar(200) NOT NULL,
  `beginDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `inDays` decimal(22,0) DEFAULT NULL,
  `inMonths` decimal(22,0) DEFAULT NULL,
  `_raw` varchar(200) DEFAULT NULL,
  `rev` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_flag`
--

DROP TABLE IF EXISTS `te_flag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_flag` (
  `id` varchar(200) NOT NULL,
  `effectLevel` varchar(200) DEFAULT NULL,
  `flagType` varchar(200) DEFAULT NULL,
  `information` text,
  `score` decimal(22,2) DEFAULT NULL,
  `rev` decimal(22,0) DEFAULT NULL,
  `noticeId` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_leftinfo`
--

DROP TABLE IF EXISTS `te_leftinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_leftinfo` (
  `id` varchar(200) NOT NULL,
  `depositsAndGuarantees` longtext,
  `executionStaff` longtext,
  `financialAbility` longtext,
  `financingConditions` longtext,
  `legalFormToBeTaken` longtext,
  `otherParticularConditions` longtext,
  `particularProfession` longtext,
  `personalSituation` longtext,
  `qualificationForTheSystem` longtext,
  `reservedContracts` longtext,
  `technicalCapacity` longtext,
  `rev` decimal(22,0) DEFAULT NULL,
  `noticeId` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_lot`
--

DROP TABLE IF EXISTS `te_lot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_lot` (
  `id` varchar(200) NOT NULL,
  `additionalInfo` longtext,
  `numb` decimal(22,0) DEFAULT NULL,
  `quantity` longtext,
  `rawCpvCodes` longtext,
  `rawNumber` varchar(200) DEFAULT NULL,
  `shortDescription` longtext,
  `title` longtext,
  `noticeId` varchar(200) DEFAULT NULL,
  `rev` decimal(22,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_notice`
--

DROP TABLE IF EXISTS `te_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_notice` (
  `id` varchar(200) NOT NULL,
  `noticeNumber` decimal(22,0) DEFAULT NULL,
  `tedUrl` varchar(200) DEFAULT NULL,
  `noticeYear` decimal(22,0) DEFAULT NULL,
  `documentFamilyId` varchar(200) DEFAULT NULL,
  `rev` decimal(22,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_objofthecontract`
--

DROP TABLE IF EXISTS `te_objofthecontract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_objofthecontract` (
  `id` varchar(200) NOT NULL,
  `additionalInfo` longtext,
  `contractTitle` longtext,
  `contractTypeInfo` longtext,
  `estimatedValue` decimal(22,0) DEFAULT NULL,
  `estimatedValueCurr` varchar(200) DEFAULT NULL,
  `financingConditions` longtext,
  `frameworkAgreement` longtext,
  `gpa` longtext,
  `lots` longtext,
  `options` longtext,
  `pcFaDps` longtext,
  `placeOfPerformance` longtext,
  `plannedStartDate` datetime DEFAULT NULL,
  `rawEstimatedValue` longtext,
  `rawPlannedStartDate` varchar(200) DEFAULT NULL,
  `rawRenewable` longtext,
  `rawRenewalCount` varchar(200) DEFAULT NULL,
  `rawTotalFinalValue` varchar(200) DEFAULT NULL,
  `rawTotalFinalValueVat` varchar(200) DEFAULT NULL,
  `renewable` decimal(1,0) DEFAULT NULL,
  `renewalCount` decimal(22,0) DEFAULT NULL,
  `shortDescription` longtext,
  `totalFinalValue` decimal(22,0) DEFAULT NULL,
  `totalFinalValueCurr` varchar(200) DEFAULT NULL,
  `totalFinalValueVat` decimal(22,2) DEFAULT NULL,
  `totalQuantity` longtext,
  `variants` longtext,
  `noticeId` varchar(200) DEFAULT NULL,
  `rev` decimal(22,0) DEFAULT NULL,
  `frameworkParticipants` decimal(22,0) DEFAULT NULL,
  `lotTitle` longtext,
  `rawLotCpvCodes` longtext,
  `awardCriteria` longtext,
  `rawLotEstimatedValue` varchar(200) DEFAULT NULL,
  `lotEstimatedValue` decimal(22,0) DEFAULT NULL,
  `lotEstimatedValueCurr` varchar(200) DEFAULT NULL,
  `offersCount` decimal(22,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_organization`
--

DROP TABLE IF EXISTS `te_organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_organization` (
  `id` varchar(200) NOT NULL,
  `code` varchar(200) DEFAULT NULL,
  `mainActivities` longtext,
  `name` longtext,
  `rawMainActivities` longtext,
  `_type` varchar(200) DEFAULT NULL,
  `rev` decimal(22,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_procedure`
--

DROP TABLE IF EXISTS `te_procedure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_procedure` (
  `id` varchar(200) NOT NULL,
  `awardCriteria` longtext,
  `electronicAuction` longtext,
  `fileRefNumber` varchar(200) DEFAULT NULL,
  `interestDeadline` datetime DEFAULT NULL,
  `invitationsDispatchDate` datetime DEFAULT NULL,
  `limitOfInvitedOperators` longtext,
  `obtainingSpecs` longtext,
  `openingConditions` longtext,
  `openingDate` datetime DEFAULT NULL,
  `previousPublication` longtext,
  `procedureTypeInfo` longtext,
  `rawInterestDeadline` varchar(200) DEFAULT NULL,
  `rawInvitationsDispatchDate` varchar(200) DEFAULT NULL,
  `reductionOfOperators` longtext,
  `renewalInfo` longtext,
  `tenderLanguage` varchar(200) DEFAULT NULL,
  `rev` decimal(22,0) DEFAULT NULL,
  `noticeId` varchar(200) DEFAULT NULL,
  `faDps` longtext,
  `gpa` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `te_relationdescriptor`
--

DROP TABLE IF EXISTS `te_relationdescriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `te_relationdescriptor` (
  `id` varchar(200) NOT NULL,
  `_type` varchar(200) DEFAULT NULL,
  `relationLeftId` varchar(200) DEFAULT NULL,
  `relationRightId` varchar(200) DEFAULT NULL,
  `additonalInfo` varchar(200) DEFAULT NULL,
  `rev` decimal(22,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-06-15  9:45:01
