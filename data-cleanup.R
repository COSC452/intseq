library(dplyr)

current_folder <- dirname(sys.frame(1)$ofile)
all_files <- list.files(paste0(current_folder, "/data"), pattern = ".csv", full.names = TRUE)

data <- lapply(all_files, read.csv, stringsAsFactors = FALSE) %>%
  {do.call(rbind, .)} %>%
  select(sequence, has_crossover, generations_taken, best_error, 
         diversity, avg_size, time_taken, found_solution)

data[ , colnames(data)] <- lapply(data[ , colnames(data)], trimws) # trim whitespace

data <- mutate(data, has_crossover = readr::parse_logical(has_crossover),
               generations_taken = readr::parse_integer(generations_taken),
               best_error = readr::parse_integer(best_error),
               diversity = readr::parse_double(diversity),
               avg_size = readr::parse_double(avg_size),
               time_taken = readr::parse_double(time_taken),
               found_solution = readr::parse_logical(found_solution))

# save clean data frame
saveRDS(data, paste0(current_folder, "/data/clean.Rds"))
