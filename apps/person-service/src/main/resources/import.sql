-- Load some test data

insert into PERSON (id, locale, title, surname, username, idnumber, gender, birthdate, favColor, website, biography,
                    organization, occupation, joinDate, maritalStatus, userAgent)
values (1, 'en-ZA', 'Mrs.', 'O''Reilly', 'armando.wisoky', '797-95-4822', 1, '1977-11-08', 'mint green',
        'http://www.korey-gleichner.org',
        'Aut eaque et velit exercitationem. Voluptatum nihil sint quo sed eos saepe. Aut officia enim unde. Et quae fugiat quas vel est. Quo eos ipsum sed inventore et. Sunt dolor quia nulla debitis porro rerum vel. Voluptates qui ab.',
        'Rice Sons', 'Architect', '2011-03-04', 'Separated', 'Opera');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (1, 'Christine');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (1, 'Fabian');
insert into PERSON_NICKNAMES (PERSON_ID, NICKNAMES)
values (1, 'Annie Moore');
insert into PERSON_COVERPHOTOS (PERSON_ID, COVERPHOTOS)
values (1, 'http://lorempixel.com/640/480/city/');
insert into PERSON_PROFILEPICTURES (PERSON_ID, PROFILEPICTURES)
values (1, 'https://s3.amazonaws.com/uifaces/faces/twitter/knilob/128.jpg');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (1, 'minerva.weber@hotmail.com');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (1, 'lavette.cummings@gmail.com');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (1, 'Always pass on what you have learned.');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (1,
        'Chuck Norris is immutable. If something''s going to change, it''s going to have to be the rest of the universe.');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (1, 'League of Legends');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (1, 'raw denim');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (1, 'Networking skills');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (1, 'Leadership');
insert into ADDRESS (ID, CODE)
values (1, '60896');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (1, '0245 Vivien Springs', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (1, 'North Elbertmouth', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (1, 'New Mexico', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (1, 'Bosnia and Herzegovina', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (1, 1);
insert into ADDRESS (ID, CODE)
values (2, '69136');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (2, '26945 Chandra Coves', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (2, 'Streichbury', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (2, 'Minnesota', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (2, 'Sweden', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (1, 2);
insert into PHONE (ID, NUMBER, TYPE)
values (1, '786-413-1620', 'Cell');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (1, 1);
insert into PHONE (ID, NUMBER, TYPE)
values (2, '1-355-531-6555', 'Home');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (1, 2);
insert into PHONE (ID, NUMBER, TYPE)
values (3, '(682) 284-5903 x48295', 'Work');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (1, 3);
insert into CREDITCARD (ID, EXPIRY, NUMBER, TYPE)
values (1, '2015-11-11', '1211-1221-1234-2201', 'laser');
insert into PERSON_CREDITCARD (PERSON_ID, CREDITCARDS_ID)
values (1, 1);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (1, 'Slack', 'joseph.sauer');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (1, 1);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (2, 'ICQ', 'roderick.homenick');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (1, 2);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (1, 'Twitter', '@patrina.kemmer');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (1, 1);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (2, 'Facebook', 'noemi.bogan');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (1, 2);

insert into PERSON (id, locale, title, surname, username, idnumber, gender, birthdate, favColor, website, biography,
                    organization, occupation, joinDate, maritalStatus, userAgent)
values (2, 'en-ZA', 'Mr.', 'Kohler', 'armando.wisoky', '373-95-3047', 0, '1977-11-08', 'maroon',
        'http://www.santos-schumm.com',
        'Aut eaque et velit exercitationem. Voluptatum nihil sint quo sed eos saepe. Aut officia enim unde. Et quae fugiat quas vel est. Quo eos ipsum sed inventore et. Sunt dolor quia nulla debitis porro rerum vel. Voluptates qui ab.',
        'Block Sons', 'Specialist', '2011-03-04', 'Married', 'Mozilla');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (2, 'Cesar');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (2, 'Junior');
insert into PERSON_NICKNAMES (PERSON_ID, NICKNAMES)
values (2, 'Sam Urai');
insert into PERSON_COVERPHOTOS (PERSON_ID, COVERPHOTOS)
values (2, 'http://lorempixel.com/320/200/city/');
insert into PERSON_PROFILEPICTURES (PERSON_ID, PROFILEPICTURES)
values (2, 'https://s3.amazonaws.com/uifaces/faces/twitter/anoff/128.jpg');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (2, 'kristie.boyle@hotmail.com');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (2, 'jessie.douglas@yahoo.com');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (2, 'Soon will I rest, yes, forever sleep. Earned it I have. Twilight is upon me, soon night must fall.');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (2, 'Chuck Norris knows the last digit of PI.');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (2, 'League of Legends');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (2, 'church-key');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (2, 'Communication');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (2, 'Proactive');
insert into ADDRESS (ID, CODE)
values (3, '39513-6916');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (3, '9722 Katie Way', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (3, 'Erinborough', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (3, 'Maine', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (3, 'Sudan', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (2, 3);
insert into ADDRESS (ID, CODE)
values (4, '38987-6464');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (4, '164 Stokes Mills', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (4, 'Mohammedstad', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (4, 'Nevada', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (4, 'Lebanon', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (2, 4);
insert into PHONE (ID, NUMBER, TYPE)
values (4, '505-954-9800', 'Cell');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (2, 4);
insert into PHONE (ID, NUMBER, TYPE)
values (5, '250-194-1387 x21248', 'Home');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (2, 5);
insert into PHONE (ID, NUMBER, TYPE)
values (6, '913.941.1008 x377', 'Work');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (2, 6);
insert into CREDITCARD (ID, EXPIRY, NUMBER, TYPE)
values (2, '2015-11-11', '1212-1221-1121-1234', 'solo');
insert into PERSON_CREDITCARD (PERSON_ID, CREDITCARDS_ID)
values (2, 2);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (3, 'Slack', 'monroe.armstrong');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (2, 3);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (4, 'ICQ', 'will.stark');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (2, 4);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (3, 'Twitter', '@cythia.yost');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (2, 3);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (4, 'Facebook', 'cecille.gibson');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (2, 4);

insert into PERSON (id, locale, title, surname, username, idnumber, gender, birthdate, favColor, website, biography,
                    organization, occupation, joinDate, maritalStatus, userAgent)
values (3, 'en-ZA', 'Ms.', 'O''Keefe', 'ermelinda.blanda', '097-87-6795', 1, '1977-11-08', 'grey',
        'http://www.lilian-hauck.net',
        'Aut eaque et velit exercitationem. Voluptatum nihil sint quo sed eos saepe. Aut officia enim unde. Et quae fugiat quas vel est. Quo eos ipsum sed inventore et. Sunt dolor quia nulla debitis porro rerum vel. Voluptates qui ab.',
        'Schroeder Inc', 'Technician', '2011-03-04', 'Never married', 'Mozilla');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (3, 'Norman');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (3, 'Magdalena');
insert into PERSON_NICKNAMES (PERSON_ID, NICKNAMES)
values (3, 'Dee Sember');
insert into PERSON_COVERPHOTOS (PERSON_ID, COVERPHOTOS)
values (3, 'http://lorempixel.com/720/348/city/');
insert into PERSON_PROFILEPICTURES (PERSON_ID, PROFILEPICTURES)
values (3, 'https://s3.amazonaws.com/uifaces/faces/twitter/mirfanqureshi/128.jpg');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (3, 'forest.dubuque@hotmail.com');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (3, 'chantal.auer@gmail.com');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (3,
        'To answer power with power, the Jedi way this is not. In this war, a danger there is, of losing who we are.');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (3, 'When Chuck Norris'' code fails to compile the compiler apologises.');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (3, 'Dota 2');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (3, 'chia');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (3, 'Confidence');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (3, 'Work under pressure');
insert into ADDRESS (ID, CODE)
values (5, '99566');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (5, '3886 Jamee Trafficway', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (5, 'New Mistiburgh', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (5, 'Pennsylvania', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (5, 'Svalbard & Jan Mayen Islands', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (3, 5);
insert into ADDRESS (ID, CODE)
values (6, '86950-8145');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (6, '8035 Nader Club', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (6, 'Brownport', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (6, 'Nevada', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (6, 'Tonga', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (3, 6);
insert into PHONE (ID, NUMBER, TYPE)
values (7, '1-485-067-4333', 'Cell');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (3, 7);
insert into PHONE (ID, NUMBER, TYPE)
values (8, '914-195-3869', 'Home');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (3, 8);
insert into PHONE (ID, NUMBER, TYPE)
values (9, '722.912.2193 x89366', 'Work');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (3, 9);
insert into CREDITCARD (ID, EXPIRY, NUMBER, TYPE)
values (3, '2015-11-11', '1212-1221-1121-1234', 'mastercard');
insert into PERSON_CREDITCARD (PERSON_ID, CREDITCARDS_ID)
values (3, 3);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (5, 'Slack', 'lieselotte.schiller');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (3, 5);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (6, 'ICQ', 'joaquin.koelpin');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (3, 6);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (5, 'Twitter', '@delmer.lebsack');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (3, 5);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (6, 'Facebook', 'stephen.haley');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (3, 6);

insert into PERSON (id, locale, title, surname, username, idnumber, gender, birthdate, favColor, website, biography,
                    organization, occupation, joinDate, maritalStatus, userAgent)
values (4, 'en-ZA', 'Mr.', 'Goyette', 'marketta.jakubowski', '347-01-8880', 0, '1977-11-08', 'green',
        'http://www.oscar-casper.net',
        'Aut eaque et velit exercitationem. Voluptatum nihil sint quo sed eos saepe. Aut officia enim unde. Et quae fugiat quas vel est. Quo eos ipsum sed inventore et. Sunt dolor quia nulla debitis porro rerum vel. Voluptates qui ab.',
        'Dare-Kemmer', 'Analyst', '2011-03-04', 'Divorced', 'Mozilla');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (4, 'Thea');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (4, 'Corinne');
insert into PERSON_NICKNAMES (PERSON_ID, NICKNAMES)
values (4, 'Frank Furter');
insert into PERSON_COVERPHOTOS (PERSON_ID, COVERPHOTOS)
values (4, 'http://lorempixel.com/1680/1050/nightlife/');
insert into PERSON_PROFILEPICTURES (PERSON_ID, PROFILEPICTURES)
values (4, 'https://s3.amazonaws.com/uifaces/faces/twitter/aka_james/128.jpg');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (4, 'stefan.ward@yahoo.com');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (4, 'synthia.kunze@hotmail.com');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (4, 'A Jedi craves not these things. You are reckless.');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (4, 'All arrays Chuck Norris declares are of infinite size, because Chuck Norris knows no bounds.');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (4, 'Overwatch');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (4, 'truffaut');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (4, 'Confidence');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (4, 'Proactive');
insert into ADDRESS (ID, CODE)
values (7, '57661');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (7, '33097 Earle Neck', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (7, 'Hoppeport', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (7, 'Mississippi', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (7, 'Bolivia', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (4, 7);
insert into ADDRESS (ID, CODE)
values (8, '91486');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (8, '67625 Tyrone Freeway', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (8, 'East Zeniaport', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (8, 'Rhode Island', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (8, 'Papua New Guinea', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (4, 8);
insert into PHONE (ID, NUMBER, TYPE)
values (10, '1-677-769-4147', 'Cell');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (4, 10);
insert into PHONE (ID, NUMBER, TYPE)
values (11, '(110) 716-8783', 'Home');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (4, 11);
insert into PHONE (ID, NUMBER, TYPE)
values (12, '1-147-624-9241 x46578', 'Work');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (4, 12);
insert into CREDITCARD (ID, EXPIRY, NUMBER, TYPE)
values (4, '2015-11-11', '1211-1221-1234-2201', 'maestro');
insert into PERSON_CREDITCARD (PERSON_ID, CREDITCARDS_ID)
values (4, 4);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (7, 'Slack', 'roy.marks');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (4, 7);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (8, 'ICQ', 'allegra.steuber');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (4, 8);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (7, 'Twitter', '@elaina.lesch');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (4, 7);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (8, 'Facebook', 'roman.breitenberg');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (4, 8);

insert into PERSON (id, locale, title, surname, username, idnumber, gender, birthdate, favColor, website, biography,
                    organization, occupation, joinDate, maritalStatus, userAgent)
values (5, 'en-ZA', 'Dr.', 'Hyatt', 'irvin.gulgowski', '733-86-4423', 0, '1977-11-08', 'orchid',
        'http://www.cassandra-lang.net',
        'Aut eaque et velit exercitationem. Voluptatum nihil sint quo sed eos saepe. Aut officia enim unde. Et quae fugiat quas vel est. Quo eos ipsum sed inventore et. Sunt dolor quia nulla debitis porro rerum vel. Voluptates qui ab.',
        'Hickle, Beatty and Treutel', 'Consultant', '2011-03-04', 'Never married', 'Mozilla');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (5, 'Eleanor');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (5, 'Joshua');
insert into PERSON_NICKNAMES (PERSON_ID, NICKNAMES)
values (5, 'Oscar Ruitt');
insert into PERSON_COVERPHOTOS (PERSON_ID, COVERPHOTOS)
values (5, 'http://lorempixel.com/g/720/348/sports/');
insert into PERSON_PROFILEPICTURES (PERSON_ID, PROFILEPICTURES)
values (5, 'https://s3.amazonaws.com/uifaces/faces/twitter/sircalebgrove/128.jpg');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (5, 'stefan.ward@yahoo.com');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (5, 'synthia.kunze@hotmail.com');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (5, 'Always two there are, no more, no less. A master and an apprentice.');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (5, 'Chuck Norris doesn''t need an OS.');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (5, 'Overwatch');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (5, 'truffaut');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (5, 'Fast learner');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (5, 'Leadership');
insert into ADDRESS (ID, CODE)
values (9, '03230');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (9, '7888 Idalia Ways', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (9, 'South Delisa', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (9, 'Rhode Island', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (9, 'Isle of Man', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (5, 9);
insert into ADDRESS (ID, CODE)
values (10, '85761');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (10, '043 Heaney Overpass', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (10, 'Simonismouth', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (10, 'Maine', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (10, 'Cambodia', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (5, 10);
insert into PHONE (ID, NUMBER, TYPE)
values (13, '102.307.7724', 'Cell');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (5, 13);
insert into PHONE (ID, NUMBER, TYPE)
values (14, '655-577-4302 x762', 'Home');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (5, 14);
insert into PHONE (ID, NUMBER, TYPE)
values (15, '579-116-8368 x0736', 'Work');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (5, 15);
insert into CREDITCARD (ID, EXPIRY, NUMBER, TYPE)
values (5, '2015-11-11', '1211-1221-1234-2201', 'discover');
insert into PERSON_CREDITCARD (PERSON_ID, CREDITCARDS_ID)
values (5, 5);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (9, 'Slack', 'roy.marks');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (5, 9);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (10, 'ICQ', 'allegra.steuber');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (5, 10);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (9, 'Twitter', '@jenifer.orn');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (5, 9);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (10, 'Facebook', 'kraig.kreiger');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (5, 10);

insert into PERSON (id, locale, title, surname, username, idnumber, gender, birthdate, favColor, website, biography,
                    organization, occupation, joinDate, maritalStatus, userAgent)
values (6, 'en-ZA', 'Mr.', 'Zemlak', 'armando.wisoky', '560-99-2165', 0, '1977-11-08', 'mint green',
        'http://www.korey-gleichner.org',
        'Aut eaque et velit exercitationem. Voluptatum nihil sint quo sed eos saepe. Aut officia enim unde. Et quae fugiat quas vel est. Quo eos ipsum sed inventore et. Sunt dolor quia nulla debitis porro rerum vel. Voluptates qui ab.',
        'Rice Sons', 'Architect', '2011-03-04', 'Separated', 'Opera');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (6, 'Masako');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (6, 'Errol');
insert into PERSON_NICKNAMES (PERSON_ID, NICKNAMES)
values (6, 'Annie Moore');
insert into PERSON_COVERPHOTOS (PERSON_ID, COVERPHOTOS)
values (6, 'http://lorempixel.com/640/480/city/');
insert into PERSON_PROFILEPICTURES (PERSON_ID, PROFILEPICTURES)
values (6, 'https://s3.amazonaws.com/uifaces/faces/twitter/knilob/128.jpg');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (6, 'minerva.weber@hotmail.com');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (6, 'lavette.cummings@gmail.com');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (6, 'Always pass on what you have learned.');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (6,
        'Chuck Norris is immutable. If something''s going to change, it''s going to have to be the rest of the universe.');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (6, 'League of Legends');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (6, 'raw denim');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (6, 'Networking skills');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (6, 'Leadership');
insert into ADDRESS (ID, CODE)
values (11, '60896');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (11, '0245 Vivien Springs', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (11, 'North Elbertmouth', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (11, 'New Mexico', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (11, 'Bosnia and Herzegovina', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (6, 11);
insert into ADDRESS (ID, CODE)
values (12, '69136');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (12, '26945 Chandra Coves', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (12, 'Streichbury', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (12, 'Minnesota', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (12, 'Sweden', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (6, 12);
insert into PHONE (ID, NUMBER, TYPE)
values (101, '786-413-1620', 'Cell');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (6, 101);
insert into PHONE (ID, NUMBER, TYPE)
values (102, '1-355-531-6555', 'Home');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (6, 102);
insert into PHONE (ID, NUMBER, TYPE)
values (103, '(682) 284-5903 x48295', 'Work');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (6, 103);
insert into CREDITCARD (ID, EXPIRY, NUMBER, TYPE)
values (6, '2015-11-11', '1211-1221-1234-2201', 'laser');
insert into PERSON_CREDITCARD (PERSON_ID, CREDITCARDS_ID)
values (6, 6);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (11, 'Slack', 'joseph.sauer');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (6, 11);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (12, 'ICQ', 'roderick.homenick');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (6, 12);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (11, 'Twitter', '@patrina.kemmer');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (6, 11);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (12, 'Facebook', 'noemi.bogan');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (6, 12);

insert into PERSON (id, locale, title, surname, username, idnumber, gender, birthdate, favColor, website, biography,
                    organization, occupation, joinDate, maritalStatus, userAgent)
values (7, 'en-ZA', 'Mrs.', 'Gleason', 'armando.wisoky', '091-07-5401', 1, '1977-11-08', 'maroon',
        'http://www.santos-schumm.com',
        'Aut eaque et velit exercitationem. Voluptatum nihil sint quo sed eos saepe. Aut officia enim unde. Et quae fugiat quas vel est. Quo eos ipsum sed inventore et. Sunt dolor quia nulla debitis porro rerum vel. Voluptates qui ab.',
        'Block Sons', 'Specialist', '2011-03-04', 'Married', 'Mozilla');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (7, 'Francis');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (7, 'Faustino');
insert into PERSON_NICKNAMES (PERSON_ID, NICKNAMES)
values (7, 'Sam Urai');
insert into PERSON_COVERPHOTOS (PERSON_ID, COVERPHOTOS)
values (7, 'http://lorempixel.com/320/200/city/');
insert into PERSON_PROFILEPICTURES (PERSON_ID, PROFILEPICTURES)
values (7, 'https://s3.amazonaws.com/uifaces/faces/twitter/anoff/128.jpg');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (7, 'kristie.boyle@hotmail.com');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (7, 'jessie.douglas@yahoo.com');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (7, 'Soon will I rest, yes, forever sleep. Earned it I have. Twilight is upon me, soon night must fall.');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (7, 'Chuck Norris knows the last digit of PI.');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (7, 'League of Legends');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (7, 'church-key');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (7, 'Communication');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (7, 'Proactive');
insert into ADDRESS (ID, CODE)
values (13, '39513-6916');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (13, '9722 Katie Way', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (13, 'Erinborough', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (13, 'Maine', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (13, 'Sudan', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (7, 13);
insert into ADDRESS (ID, CODE)
values (14, '38987-6464');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (14, '164 Stokes Mills', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (14, 'Mohammedstad', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (14, 'Nevada', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (14, 'Lebanon', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (7, 14);
insert into PHONE (ID, NUMBER, TYPE)
values (104, '505-954-9800', 'Cell');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (7, 104);
insert into PHONE (ID, NUMBER, TYPE)
values (105, '250-194-1387 x21248', 'Home');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (7, 105);
insert into PHONE (ID, NUMBER, TYPE)
values (106, '913.941.1008 x377', 'Work');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (7, 106);
insert into CREDITCARD (ID, EXPIRY, NUMBER, TYPE)
values (7, '2015-11-11', '1212-1221-1121-1234', 'solo');
insert into PERSON_CREDITCARD (PERSON_ID, CREDITCARDS_ID)
values (7, 7);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (13, 'Slack', 'monroe.armstrong');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (7, 13);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (14, 'ICQ', 'will.stark');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (7, 14);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (13, 'Twitter', '@cythia.yost');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (7, 13);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (14, 'Facebook', 'cecille.gibson');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (7, 14);

insert into PERSON (id, locale, title, surname, username, idnumber, gender, birthdate, favColor, website, biography,
                    organization, occupation, joinDate, maritalStatus, userAgent)
values (8, 'en-ZA', 'Mrs.', 'Pfeffer', 'ermelinda.blanda', '539-70-2014', 1, '1977-11-08', 'grey',
        'http://www.lilian-hauck.net',
        'Aut eaque et velit exercitationem. Voluptatum nihil sint quo sed eos saepe. Aut officia enim unde. Et quae fugiat quas vel est. Quo eos ipsum sed inventore et. Sunt dolor quia nulla debitis porro rerum vel. Voluptates qui ab.',
        'Schroeder Inc', 'Technician', '2011-03-04', 'Never married', 'Mozilla');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (8, 'Frances');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (8, 'Domenic');
insert into PERSON_NICKNAMES (PERSON_ID, NICKNAMES)
values (8, 'Dee Sember');
insert into PERSON_COVERPHOTOS (PERSON_ID, COVERPHOTOS)
values (8, 'http://lorempixel.com/720/348/city/');
insert into PERSON_PROFILEPICTURES (PERSON_ID, PROFILEPICTURES)
values (8, 'https://s3.amazonaws.com/uifaces/faces/twitter/mirfanqureshi/128.jpg');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (8, 'forest.dubuque@hotmail.com');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (8, 'chantal.auer@gmail.com');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (8,
        'To answer power with power, the Jedi way this is not. In this war, a danger there is, of losing who we are.');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (8, 'When Chuck Norris'' code fails to compile the compiler apologises.');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (8, 'Dota 2');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (8, 'chia');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (8, 'Confidence');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (8, 'Work under pressure');
insert into ADDRESS (ID, CODE)
values (15, '99566');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (15, '3886 Jamee Trafficway', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (15, 'New Mistiburgh', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (15, 'Pennsylvania', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (15, 'Svalbard & Jan Mayen Islands', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (8, 15);
insert into ADDRESS (ID, CODE)
values (16, '86950-8145');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (16, '8035 Nader Club', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (16, 'Brownport', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (16, 'Nevada', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (16, 'Tonga', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (8, 16);
insert into PHONE (ID, NUMBER, TYPE)
values (107, '1-485-067-4333', 'Cell');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (8, 107);
insert into PHONE (ID, NUMBER, TYPE)
values (108, '914-195-3869', 'Home');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (8, 108);
insert into PHONE (ID, NUMBER, TYPE)
values (109, '722.912.2193 x89366', 'Work');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (8, 109);
insert into CREDITCARD (ID, EXPIRY, NUMBER, TYPE)
values (8, '2015-11-11', '1212-1221-1121-1234', 'mastercard');
insert into PERSON_CREDITCARD (PERSON_ID, CREDITCARDS_ID)
values (8, 8);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (15, 'Slack', 'lieselotte.schiller');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (8, 15);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (16, 'ICQ', 'joaquin.koelpin');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (8, 16);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (15, 'Twitter', '@delmer.lebsack');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (8, 15);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (16, 'Facebook', 'stephen.haley');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (8, 16);

insert into PERSON (id, locale, title, surname, username, idnumber, gender, birthdate, favColor, website, biography,
                    organization, occupation, joinDate, maritalStatus, userAgent)
values (9, 'en-ZA', 'Miss', 'Doyle', 'marketta.jakubowski', '029-18-5986', 1, '1977-11-08', 'green',
        'http://www.oscar-casper.net',
        'Aut eaque et velit exercitationem. Voluptatum nihil sint quo sed eos saepe. Aut officia enim unde. Et quae fugiat quas vel est. Quo eos ipsum sed inventore et. Sunt dolor quia nulla debitis porro rerum vel. Voluptates qui ab.',
        'Dare-Kemmer', 'Analyst', '2011-03-04', 'Divorced', 'Mozilla');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (9, 'Louis');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (9, 'Eugenia');
insert into PERSON_NICKNAMES (PERSON_ID, NICKNAMES)
values (9, 'Frank Furter');
insert into PERSON_COVERPHOTOS (PERSON_ID, COVERPHOTOS)
values (9, 'http://lorempixel.com/1680/1050/nightlife/');
insert into PERSON_PROFILEPICTURES (PERSON_ID, PROFILEPICTURES)
values (9, 'https://s3.amazonaws.com/uifaces/faces/twitter/aka_james/128.jpg');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (9, 'stefan.ward@yahoo.com');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (9, 'synthia.kunze@hotmail.com');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (9, 'A Jedi craves not these things. You are reckless.');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (9, 'All arrays Chuck Norris declares are of infinite size, because Chuck Norris knows no bounds.');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (9, 'Overwatch');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (9, 'truffaut');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (9, 'Confidence');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (9, 'Proactive');
insert into ADDRESS (ID, CODE)
values (17, '57661');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (17, '33097 Earle Neck', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (17, 'Hoppeport', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (17, 'Mississippi', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (17, 'Bolivia', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (9, 17);
insert into ADDRESS (ID, CODE)
values (18, '91486');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (18, '67625 Tyrone Freeway', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (18, 'East Zeniaport', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (18, 'Rhode Island', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (18, 'Papua New Guinea', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (9, 18);
insert into PHONE (ID, NUMBER, TYPE)
values (110, '1-677-769-4147', 'Cell');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (9, 110);
insert into PHONE (ID, NUMBER, TYPE)
values (111, '(110) 716-8783', 'Home');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (9, 111);
insert into PHONE (ID, NUMBER, TYPE)
values (112, '1-147-624-9241 x46578', 'Work');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (9, 112);
insert into CREDITCARD (ID, EXPIRY, NUMBER, TYPE)
values (9, '2015-11-11', '1211-1221-1234-2201', 'maestro');
insert into PERSON_CREDITCARD (PERSON_ID, CREDITCARDS_ID)
values (9, 9);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (17, 'Slack', 'roy.marks');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (9, 17);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (18, 'ICQ', 'allegra.steuber');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (9, 18);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (17, 'Twitter', '@elaina.lesch');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (9, 17);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (18, 'Facebook', 'roman.breitenberg');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (9, 18);

insert into PERSON (id, locale, title, surname, username, idnumber, gender, birthdate, favColor, website, biography,
                    organization, occupation, joinDate, maritalStatus, userAgent)
values (10, 'en-ZA', 'Dr.', 'Doyle', 'irvin.gulgowski', '287-58-0690', 0, '1977-11-08', 'orchid',
        'http://www.cassandra-lang.net',
        'Aut eaque et velit exercitationem. Voluptatum nihil sint quo sed eos saepe. Aut officia enim unde. Et quae fugiat quas vel est. Quo eos ipsum sed inventore et. Sunt dolor quia nulla debitis porro rerum vel. Voluptates qui ab.',
        'Hickle, Beatty and Treutel', 'Consultant', '2011-03-04', 'Never married', 'Mozilla');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (10, 'Carita');
insert into PERSON_NAMES (PERSON_ID, NAMES)
values (10, 'Danny');
insert into PERSON_NICKNAMES (PERSON_ID, NICKNAMES)
values (10, 'Oscar Ruitt');
insert into PERSON_COVERPHOTOS (PERSON_ID, COVERPHOTOS)
values (10, 'http://lorempixel.com/g/720/348/sports/');
insert into PERSON_PROFILEPICTURES (PERSON_ID, PROFILEPICTURES)
values (10, 'https://s3.amazonaws.com/uifaces/faces/twitter/sircalebgrove/128.jpg');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (10, 'stefan.ward@yahoo.com');
insert into PERSON_EMAILADDRESSES (PERSON_ID, EMAILADDRESSES)
values (10, 'synthia.kunze@hotmail.com');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (10, 'Always two there are, no more, no less. A master and an apprentice.');
insert into PERSON_TAGLINES (PERSON_ID, TAGLINES)
values (10, 'Chuck Norris doesn''t need an OS.');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (10, 'Overwatch');
insert into PERSON_INTERESTS (PERSON_ID, INTERESTS)
values (10, 'truffaut');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (10, 'Fast learner');
insert into PERSON_SKILLS (PERSON_ID, SKILLS)
values (10, 'Leadership');
insert into ADDRESS (ID, CODE)
values (19, '03230');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (19, '7888 Idalia Ways', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (19, 'South Delisa', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (19, 'Rhode Island', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (19, 'Isle of Man', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (10, 19);
insert into ADDRESS (ID, CODE)
values (20, '85761');
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (20, '043 Heaney Overpass', 0);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (20, 'Simonismouth', 1);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (20, 'Maine', 2);
insert into ADDRESS_LINES (ADDRESS_ID, LINES, LINES_ORDER)
values (20, 'Cambodia', 3);
insert into PERSON_ADDRESS (PERSON_ID, ADDRESSES_ID)
values (10, 20);
insert into PHONE (ID, NUMBER, TYPE)
values (113, '102.307.7724', 'Cell');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (10, 113);
insert into PHONE (ID, NUMBER, TYPE)
values (114, '655-577-4302 x762', 'Home');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (10, 114);
insert into PHONE (ID, NUMBER, TYPE)
values (115, '579-116-8368 x0736', 'Work');
insert into PERSON_PHONE (PERSON_ID, PHONENUMBERS_ID)
values (10, 115);
insert into CREDITCARD (ID, EXPIRY, NUMBER, TYPE)
values (10, '2015-11-11', '1211-1221-1234-2201', 'discover');
insert into PERSON_CREDITCARD (PERSON_ID, CREDITCARDS_ID)
values (10, 10);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (19, 'Slack', 'roy.marks');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (10, 19);
insert into IMCLIENT (ID, IDENTIFIER, IM)
values (20, 'ICQ', 'allegra.steuber');
insert into PERSON_IMCLIENT (PERSON_ID, IMCLIENTS_ID)
values (10, 20);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (19, 'Twitter', '@jenifer.orn');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (10, 19);
insert into SOCIALMEDIA (ID, NAME, USERNAME)
values (20, 'Facebook', 'kraig.kreiger');
insert into PERSON_SOCIALMEDIA (PERSON_ID, SOCIALMEDIAS_ID)
values (10, 20);
