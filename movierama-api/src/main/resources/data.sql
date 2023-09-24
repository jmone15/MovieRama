--- INSERT USER DATA
INSERT INTO public."user" (email, external_id, name, password, role, username) VALUES ('jdoe@gmail.com', 'ccd3eaee-7e8a-4ef1-a988-c285a8470eac', 'Joe Doe', '$2a$10$82v3soqGDGG2ANNCsz5nBOngFTUlEDs3xSo945TTuPvhmyboh1oCy', 'ROLE_USER', 'joe');
INSERT INTO public."user" (email, external_id, name, password, role, username) VALUES ('jane@hotmail.com', '06285776-91c7-4e13-b0b8-cf7b5b7498fc', 'Jane Doe', '$2a$10$eSdONB7DWfR.o.FzAAkzj.dNJAq8vStxwoSZ0os5CT392nZ.1JAia', 'ROLE_USER', 'jane');
INSERT INTO public."user" (email, external_id, name, password, role, username) VALUES ('jmone@gmail.com', 'b14e3e75-ca23-445b-ac8a-d1ef1e2ea5b7', 'Jurgen Mone', '$2a$10$IeeWj2HBOWPptgtLJbK9oOMdToy/SdIb7vs5fkayHiEVNVKzKYMje', 'ROLE_USER', 'jmone');
INSERT INTO public."user" (email, external_id, name, password, role, username) VALUES ('admin@system.gr', '5eb48bfa-0488-42ac-8522-a155079381b8', 'Admin User', '$2a$10$xkiHXRfnnNy.O/V6R4utleIfAj/rVNEh9lj0Bv19IjUcbr1QguqAG', 'ROLE_ADMIN', 'admin');

--- INSERT MOVIE DATA
INSERT INTO public.movie (description, external_id, publication_date, title, user_id) VALUES ('Two Jedi escape a hostile blockade to find allies and come across a young boy who may bring balance to the Force, but the long dormant Sith resurface to claim their original glory.', '3986cfb4-597b-4ffd-9a35-25064e995f0f', '2023-09-24 19:41:44.557897', 'Star Wars: Episode I - The Phantom Menace', 1);
INSERT INTO public.movie (description, external_id, publication_date, title, user_id) VALUES ('Ten years after initially meeting, Anakin Skywalker shares a forbidden romance with Padmé Amidala, while Obi-Wan Kenobi discovers a secret clone army crafted for the Jedi.', '4e7f1cd7-9504-4a6d-9cd6-5ca1a2adea8d', '2023-09-24 19:42:57.759534', 'Star Wars: Episode II - Attack of the Clones', 1);
INSERT INTO public.movie (description, external_id, publication_date, title, user_id) VALUES ('Three years into the Clone Wars, Obi-Wan pursues a new threat, while Anakin is lured by Chancellor Palpatine into a sinister plot to rule the galaxy.', '8d6db61f-4126-4723-9e19-548c844af6b3', '2023-09-24 19:43:32.198611', 'Star Wars: Episode III - Revenge of the Sith', 1);
INSERT INTO public.movie (description, external_id, publication_date, title, user_id) VALUES ('A mind-bending thriller where thieves enter dreams to steal secrets. Dom Cobb leads a journey through layers of dreams, blurring reality.', '27c2f5f8-4607-4dd6-b54f-25fb1488e25f', '2023-09-24 20:44:12.365271', 'Inception', 2);
INSERT INTO public.movie (description, external_id, publication_date, title, user_id) VALUES ('A powerful drama about Andy''s wrongful imprisonment, his friendship with Red, and their quest for hope and justice in Shawshank prison.', '78f5b77f-4909-41f5-bd9e-0a33eaa9b47d', '2023-09-24 20:44:26.802985', 'The Shawshank Redemption', 2);
INSERT INTO public.movie (description, external_id, publication_date, title, user_id) VALUES ('A whimsical comedy set in 1930s Zubrowka. Gustave, a legendary concierge, and Zero embark on a madcap adventure to clear Gustave''s name and recover a priceless painting.', '859976ad-d5fe-4408-a178-23b037d1ae87', '2023-09-24 20:44:40.291851', 'The Grand Budapest Hotel', 2);
INSERT INTO public.movie (description, external_id, publication_date, title, user_id) VALUES ('Tarantino''s crime masterpiece weaves interconnected stories of gangsters, hitmen, and dark humor, exploring themes of redemption.', '1921a055-2959-4612-962f-3a0a97eb0278', '2023-09-24 20:46:53.757894', 'Pulp Fiction', 3);
INSERT INTO public.movie (description, external_id, publication_date, title, user_id) VALUES ('Epic fantasy follows Frodo''s journey to destroy a powerful ring with a diverse Fellowship in Middle-earth.', 'd9ca41d3-d95a-4f96-a3a3-f3a11d908009', '2023-09-24 20:47:11.183829', 'The Lord of the Rings: The Fellowship of the Ring', 3);
INSERT INTO public.movie (description, external_id, publication_date, title, user_id) VALUES ('Tom Hanks stars in the heartwarming tale of Forrest Gump, a simple man who influences decades of American history.', '2837b082-a595-476e-9ae2-09d2d6d79821', '2023-09-24 20:47:23.959949', 'Forrest Gump', 3);

--- INSERT MOVIE_LIKES_ASSOCIATIONS
INSERT INTO public.movie_likes_associations (movie_id, user_id) VALUES (3, 3);
INSERT INTO public.movie_likes_associations (movie_id, user_id) VALUES (1, 3);
INSERT INTO public.movie_likes_associations (movie_id, user_id) VALUES (6, 3);
INSERT INTO public.movie_likes_associations (movie_id, user_id) VALUES (9, 1);
INSERT INTO public.movie_likes_associations (movie_id, user_id) VALUES (8, 1);
INSERT INTO public.movie_likes_associations (movie_id, user_id) VALUES (7, 1);
INSERT INTO public.movie_likes_associations (movie_id, user_id) VALUES (6, 1);
INSERT INTO public.movie_likes_associations (movie_id, user_id) VALUES (5, 1);
INSERT INTO public.movie_likes_associations (movie_id, user_id) VALUES (4, 1);
INSERT INTO public.movie_likes_associations (movie_id, user_id) VALUES (9, 2);

--- INSERT MOVIE_HATES_ASSOCIATIONS
INSERT INTO public.movie_hates_associations (movie_id, user_id) VALUES (2, 2);
INSERT INTO public.movie_hates_associations (movie_id, user_id) VALUES (2, 3);
INSERT INTO public.movie_hates_associations (movie_id, user_id) VALUES (3, 2);
INSERT INTO public.movie_hates_associations (movie_id, user_id) VALUES (1, 2);
INSERT INTO public.movie_hates_associations (movie_id, user_id) VALUES (5, 3);
INSERT INTO public.movie_hates_associations (movie_id, user_id) VALUES (4, 3);
INSERT INTO public.movie_hates_associations (movie_id, user_id) VALUES (8, 2);
INSERT INTO public.movie_hates_associations (movie_id, user_id) VALUES (7, 2);

