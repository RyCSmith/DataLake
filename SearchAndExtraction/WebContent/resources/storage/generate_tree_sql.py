f = open('/Users/ryancsmith/Downloads/new.txt', 'w')
letters = "abcdefghijklmnopqrstuvwxyz1234567890!@$%&*"
counter = 0
for i in range(214,235):
	for j in range (0,2):
		f.write("insert into ii_table values('" + str(letters[counter]) +str(letters[counter]) + str(letters[counter]) +"', " + str(i) + ");\n")
		counter += 1

# counter = 1
# for i in "abcdefghijklmnopqrstuvwx":
# 	f.write("insert into word_table values (" + str(counter) + ", '" + str(i)+ "'" + ");\n")
# 	counter += 1

counter = 1
letter_counter = 0
letters = "abcdefghijklmnopqrstuvwxyz1234567890!@$%&*+"
for i in range(214,235):
	f.write("insert into node_table values (" + str(i) + ", '" + str(letters[letter_counter]) + str(letters[letter_counter]) + str(letters[letter_counter]) + "', '" + str(letters[letter_counter + 1])+ str(letters[letter_counter + 1])+ str(letters[letter_counter + 1]) + "', " + "5);\n")
	letter_counter += 2

# for i in range(0,11):
# 	f.write("insert into edge_table values (\n")

for i in range(214,234):
	f.write("insert into edge_table values (" + str(i) + ", " + str(i+1) +", 'T');\n")

f.close