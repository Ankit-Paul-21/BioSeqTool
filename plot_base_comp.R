# R-Script for plotting base composition

png(filename = "base_composition.png", width = 1600, height = 1200, res = 300)

dat <- read.table(file = "base_composition.txt", header = FALSE)
#print(dat)

base <- dat$V1
#print(base)

base_composition <- dat$V2
#print(base_composition)

bar_positions <- barplot(height = base_composition, main = "Base Composition", ylab = "% Prevalence", names.arg = base,
border = "black", col = c("red", "yellow", "green", "blue"))

text(x = bar_positions, y = (base_composition / 2 ), labels = base_composition, font = 2, cex = 1.1)
