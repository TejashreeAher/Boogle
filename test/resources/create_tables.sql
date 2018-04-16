CREATE database Boogle;

USE Boogle;

CREATE TABLE Books(
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    author VARCHAR(50),
    PRIMARY KEY (id),
    UNIQUE (title, author)
);

CREATE TABLE Pages(
    id BIGINT NOT NULL AUTO_INCREMENT,
    book_id BIGINT,
    page_number INT NOT NULL,
    chapter VARCHAR(50),
    content VARCHAR(1000),
    PRIMARY KEY (id),
    UNIQUE (book_id, page_number),
    FOREIGN KEY (book_id)
            REFERENCES Books(id)
            ON DELETE CASCADE
);

INSERT INTO Books(title, author) VALUES ("India Unbound", "Gurcharan Das");
INSERT INTO Books(title, author) VALUES ("The Da Vinci Code", "Dan Brown");

INSERT INTO Pages(book_id, page_number, chapter, content) VALUES(2, 3, "Chapter 1", "Robert Langdon awoke slowly. A telephone was ringing in the darkness-a tinny, unfamiliar ring. He fumbled for the bedside lamp and turned it on. Squinting at his surroundings he saw a plush Renaissance bedroom with Louis XVI furniture, hand-frescoed walls, and a colossal mahogany four-poster bed. Where the hell am I? The jacquard bathrobe hanging on his bedpost bore the monogram: HOTEL RITZ PARIS. Slowly, the fog began to lift.");
INSERT INTO Pages(book_id, page_number, chapter, content) VALUES(2, 4, "Chapter 1", "Langdon picked up the receiver. \"Hello?\" \"Monsieur Langdon?\" a man’s voice said. \"I hope I have not awoken you?\" Dazed, Langdon looked at the bedside clock. It was 12:32 A.M. He had been asleep only an hour, but he felt like the dead. \"This is the concierge, monsieur. I apologize for this intrusion, but you have a visitor. He insists it is urgent.\" Langdon still felt fuzzy. A visitor? His eyes focused now on a crumpled  yer on his bedside table. THE AMERICAN UNIVERSITY OF PARIS proudly presents an evening with Robert Langdon Professor of Religious Symbology, Harvard University");
INSERT INTO Pages(book_id, page_number, chapter, content) VALUES(2, 5, "Chapter 2", "Langdon felt a sudden surge of uneasiness. He and the revered curator Jacques Saunière had been slated to meet for drinks after Langdon’s lecture tonight, but Saunière had never shown up.");
INSERT INTO Pages(book_id, page_number, chapter, content) VALUES(1, 1, "Pre Independence", "Das shows how India’s policies after 1947 condemned the nation to a hobbled economy until 1991, when the government instituted sweeping reforms that paved the way for extraordinary growth. Das traces these developments and tells the stories of the major players from Nehru through today");
INSERT INTO Pages(book_id, page_number, chapter, content) VALUES(1, 2, "post Independence", "India today is a vibrant free-market democracy, a nation well on its way to overcoming decades of widespread poverty. The nation’s rise is one of the great international stories of the late twentieth century, and in India Unbound the acclaimed columnist Gurcharan Das offers a sweeping economic history of India from independence to the new millennium. ");
INSERT INTO Pages(book_id, page_number, chapter, content) VALUES(1, 4, "post Indepdence", "Impassioned, erudite, and eminently readable, India Unbound is a must for anyone interested in the global economy and its future.");
INSERT INTO Pages(book_id, page_number, chapter, content) VALUES(2, 10, "Chapter 1", "The image was gruesome and profoundly strange, bringing with it an unsettling sense of deja vu. A little over a year ago, Langdon had received a photograph of a corpse and a similar re- quest for help. Twenty-four hours later, he had almost lost his life inside Vatican City. This photo was entirely different, and yet something about the scenario felt disquietingly familiar. The agent checked his watch.");
INSERT INTO Pages(book_id, page_number, chapter, content) VALUES()
INSERT INTO Pages(book_id, page_number, chapter, content) VALUES()
INSERT INTO Pages(book_id, page_number, chapter, content) VALUES()
INSERT INTO Pages(book_id, page_number, chapter, content) VALUES()
INSERT INTO Pages(book_id, page_number, chapter, content) VALUES()


