create database if not exists triviaLib;

use trivialib;
drop table if exists qna;
create table qna (
    id int,
    question varchar(100),
    aanswer1 varchar(50),
    aanswer2 varchar(50),
    aanswer3 varchar(50),
    aanswer4 varchar(50),
    correctAns varchar(1));
    
insert into qna values (101, 'What does “www” stand for in a website browser?', 'World Wide Web', 'Wild Wild West', 'Worldly Workers Weekly', 'Wide Woke World', 'A');
insert into qna values (102, 'How long is an Olympic swimming pool (in meters)?', '30meters', '50 meters', '100 meters', '60 meters', 'B');
insert into qna values (103, 'What countries made up the original Axis powers in World War II?', 'Singapore, Malaysia, Indonesia', 'Russia,  America, Britan', 'Germany, Italy, and Japan', 'Spain, Italy, France', 'C');
insert into qna values (104, 'Which country do cities of Perth, Adelade & Brisbane belong to?', 'Canada', 'United Kingdoms', 'America', 'Australia', 'D');
insert into qna values (105, 'What is "cynophobia"?', 'Fear of Dogs', 'Fear of Cats', 'Fear of Holes', 'Fear of Insects', 'A');
insert into qna values (106, 'Who named the Pacific Ocean?', 'Cristopher Columbus', 'Ferdinand Magellan', 'Sir Stanford Raffles', 'Plato', 'B');
insert into qna values (107, 'What were plague doctor masks filled with?', 'Poop', 'Nothing', 'Herbs and Spices', 'Holy Water', 'C');
insert into qna values (108, 'Who is Arguably the riches man in History?', 'Augustus Caesar', 'Nikolai Alexandrovich Romanov ', 'William The Conqueror', 'Mansa Musa', 'D');
insert into qna values (109, 'Which Romanov Daughters was belived to have Survived the assasination of the Romanovs?', 'Anastasia', 'Maria', 'Olga', 'Tatiana', 'A');
insert into qna values (110, 'What is the name of the largest ocean on earth?', 'Indian', 'Pacific', 'Arctic', 'Atlantic', 'B');
insert into qna values (111, 'Which is the only edible food that never goes bad?', 'Chocolate', 'Beer', 'Honey', 'Cheese', 'C');
insert into qna values (112, 'How Many poekmon were there in the original game?', '120', '100', '251', '151', 'D');
insert into qna values (113, 'What Anime Studio Produced Neon Genasis Evangelion', 'Gainax', 'Bones', 'Trigger', 'Shaft', 'A');
insert into qna values (114, 'Which country do cities of Perth, Adelade & Brisbane belong to?', 'Canada', 'United Kingdoms', 'America', 'Australia', 'B');
insert into qna values (115, 'What was Roy Mustang called in Full Metal ALchemist', 'Silver Alchemist', 'Armstrong Alchemist', 'Flame Alchemist', 'Full Metal Alchemist', 'C');
insert into qna values (116, 'What Mythology wsa Mercury From?', 'Norse', 'Mesopotamian', 'Greek', 'Roman', 'D');
insert into qna values (117, 'Other then Sunday and Monday, the days of the week are named after Norse gods accept?', 'Saturday', 'Tuesday', 'Wednesday', 'Friday', 'A');
insert into qna values (118, 'Which is more likely to kill someone?', 'A Vending Machine', 'A coconut', 'A Cow', 'A shark', 'B');
insert into qna values (119, 'Who is Peter Parker?', 'Who?', 'Doctor Strange', 'Spiderman', 'Ironman', 'C');
insert into qna values (120, 'What is an Ultrman normally Made of?', 'Rubber', 'Metal', 'Flesh', 'Light', 'D');


select * from qna;


drop table if exists Stats;
create table stats(
    Id int,
    choice1 int,
    choice2 int,
    choice3 int,
    choice4 int,
    numCorrect int,
    timesAskeed int);

insert into Stats values (101, 0 , 0 , 0, 0, 0);
insert into Stats values (102, 0 , 0 , 0, 0, 0);
insert into Stats values (103, 0 , 0 , 0, 0, 0);
insert into Stats values (104, 0 , 0 , 0, 0, 0);
insert into Stats values (105, 0 , 0 , 0, 0, 0);
insert into Stats values (106, 0 , 0 , 0, 0, 0);
insert into Stats values (107, 0 , 0 , 0, 0, 0);
insert into Stats values (108, 0 , 0 , 0, 0, 0);
insert into Stats values (109, 0 , 0 , 0, 0, 0);
insert into Stats values (110, 0 , 0 , 0, 0, 0);
insert into Stats values (111, 0 , 0 , 0, 0, 0);
insert into Stats values (112, 0 , 0 , 0, 0, 0);
insert into Stats values (113, 0 , 0 , 0, 0, 0);
insert into Stats values (114, 0 , 0 , 0, 0, 0);
insert into Stats values (115, 0 , 0 , 0, 0, 0);
insert into Stats values (116, 0 , 0 , 0, 0, 0);
insert into Stats values (117, 0 , 0 , 0, 0, 0);
insert into Stats values (118, 0 , 0 , 0, 0, 0);
insert into Stats values (119, 0 , 0 , 0, 0, 0);
insert into Stats values (120, 0 , 0 , 0, 0, 0);


select * from Stats;

drop table if exists Asked;
create table stats(
    QNo int,
    Id int);

select * from Asked;