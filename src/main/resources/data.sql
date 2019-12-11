-- role
insert into role(id, name) values (1, 'ADMIN');
insert into role(id, name) values (2, 'VISITOR');


-- permisson
insert into permission(id, name) values (1, 'ADD_BOOK');
insert into permission(id, name) values (2, 'UPDATE_BOOK');
insert into permission(id, name) values (3, 'DELETE_BOOK');
insert into permission(id, name) values (4, 'DELETE_VISITOR');
insert into permission(id, name) values (5, 'BUY_BOOK');


-- role_permission
insert into role_permission(id_role, id_permission) values (1, 1);
insert into role_permission(id_role, id_permission) values (1, 2);
insert into role_permission(id_role, id_permission) values (1, 3);
insert into role_permission(id_role, id_permission) values (1, 4);
insert into role_permission(id_role, id_permission) values (2, 5);


-- book
insert into book(id, author, description, image, price, publisher, publishing_year, title)
values(1, 'Antoine de Saint-Exupery', 'An amazing story about a boyfriend who befriended a rose.', 'images/book_theLittlePrince.jpg', 849, 'MagicBook publishing', 2010, 'The little prince');
insert into book(id, author, description, image, price, publisher, publishing_year, title)
values(2, 'Ivo Andric', 'The writer discharges the current mood, seeing an occurrence, personality, impression from the path or thought that preoccupies him.', 'images/book_signsByTheRoadside.jpg', 489, 'Delfi publishing', 2017, 'Signs by the roadside');
insert into book(id, author, description, image, price, publisher, publishing_year, title)
values(3, 'Henrik Sjenkjevic', 'An adventure novel that follows the perilous journey of two young men through the wild African deserts and rainforests.', 'images/book_throughTheDesertAndTheRainforest.jpg', 1475, 'Code publishing', 2009, 'Through the desert and the rainforest');
insert into book(id, author, description, image, price, publisher, publishing_year, title)
values(4, 'Nora Roberts', 'The sickness came on suddenly, and spread quickly. The fear spread even faster. Within weeks, everything people counted on began to fail them.', 'images/book_yearOne.jpg', 779, 'MagicBook publishing', 2019, 'Year one');
insert into book(id, author, description, image, price, publisher, publishing_year, title)
values(5, 'Cecily von Ziegesar', 'Welcome to New York Citys Upper East Side, where the girls are dazzling, the guys are gorgeous, and the summer heat is the perfect excuse to throw a fabulous roof-deck pool party.', 'images/book_onlyInYourDreams.jpg', 399, 'Laguna publishing', 2014, 'Only in your dreams');
insert into book(id, author, description, image, price, publisher, publishing_year, title)
values(6, 'Khaled Hosseini', 'This powerful debut novel tells the story of fierce cruelty and fierce love being redeemed.', 'images/book_theKiteRunner.jpg', 699, 'Contrast publishing', 2015, 'The kite runner');
insert into book(id, author, description, image, price, publisher, publishing_year, title)
values(7, 'Amanda Quick', 'Driven by dark, smoldering passions and a tragic secret buried deep within his soul, Blade has all of London cowering at his feet, but not Emily...', 'images/book_scandal.jpg', 623, 'MagicBook publishing', 2016, 'Scandal');
insert into book(id, author, description, image, price, publisher, publishing_year, title)
values(8, 'Fyodor Dostoevsky', 'Rodion Romanovich Raskoljnikov, an ambitious law student, decides to kill and rob Alyona Ivanovna, a hated old woman who exploits people with interest.', 'images/book_crimeAndPunishment.jpg', 1359, 'Delfi publishing', 2012, 'Crime and punishment');
insert into book(id, author, description, image, price, publisher, publishing_year, title)
values(9, 'Sophie Kinsella', 'When a handsome stranger in a cafe asks her to keep his laptop for a moment, Fixie not only agrees but ultimately saves the laptop from disaster. To thank her, the owner of the computer, Sebastian quickly fills the bill...', 'images/book_iOweYouOne.jpg', 829, 'Code publishing', 2013, 'I owe you one');


-- shopping_cart
insert into shopping_cart(id) values(1);
insert into shopping_cart(id) values(2);
insert into shopping_cart(id) values(3);
insert into shopping_cart(id) values(4);


-- myUser
-- admin@email.com; lozinka@L123
insert into my_user(id, activated_account, address, email, name, password, phone, surname, user_type, id_role, id_shopping_cart) 
values (1, 1, 'Safarikova 17b', 'qlTICniImOpK2yb+admin==', 'Admin', '$2a$10$5AYDJF/7JuhrYDoluKeDxejdk0qccGp5UwtnTwTnKvVeQzO0oQnd.', '0602498385', 'Admin', 'ADMIN', 1, 1);
-- markomarkovic@email.com; marko@M123
insert into my_user(id, activated_account, address, email, name, password, phone, surname, user_type, id_role, id_shopping_cart) 
values (2, 1, 'Hilton avenue 3708', 'sScEtadQfv70RoVzlEQrRN+mgPwaIE4C/Y8kl+Zf6vk=', 'Marko', '$2a$10$y47ErAahzPLbi8G5Fs12UeGIV/MJK0LSrNhe3OlamxVusSBLGGtrm', '0630321587', 'Markovic', 'VISITOR', 2, 2);
-- petarpetrovic@email.com; petar@P123
insert into my_user(id, activated_account, address, email, name, password, phone, surname, user_type, id_role, id_shopping_cart) 
values (3, 1, 'First Street 53792', 'fNPQ72cDsRGrD0/Ll7ca9lACsAbjY/pe+FiCoSH+Qtg=', 'Petar', '$2a$10$wi2eHxEK6jMNdxkx1701R.33bpbkgBknTq1ah4jORj3mKcQe7wIMK', '0640751193', 'Petrovic', 'VISITOR', 2, 3);
-- nikolanikolic@email.com; nikola@N123
insert into my_user(id, activated_account, address, email, name, password, phone, surname, user_type, id_role, id_shopping_cart) 
values (4, 1, 'Park Maddison Square 13626', 'vJ95rkPEqJKWRyJJZAViivPz3cLGgcK1CQlxVrH5wj4=', 'Nikola', '$2a$10$Qc4bo7aV6Pl1Gr.B3uyHn.bSWuiGGZUSig3KevfbyM3ABqZ/xkID2', '0659072815', 'Nikolic', 'VISITOR', 2, 4);


-- message