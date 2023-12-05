use std::{
    env,
    fs::File,
    io::{BufRead, BufReader},
};

fn main() -> Result<(), &'static str> {
    let args: Vec<String> = env::args().collect();
    if args.len() < 2 {
        return Err("Missing argument: filename");
    }
    let filename = &args[1];
    let file = File::open(filename).unwrap();
    let reader = BufReader::new(file);

    let mut sum = 0;
    for line in reader.lines() {
        let line = line.unwrap();
        /* part 1
        if !forbidden(&line) && three_vowels(&line) && double_letter(&line) {
            sum += 1
        }
        */
        if in_between(&line) && contains_pair(&line) {
            sum += 1;
        }
    }

    println!("{sum}");
    Ok(())
}

fn forbidden(line: &String) -> bool {
    for (i, _) in line.chars().enumerate() {
        if i < line.len() - 1 {
            let letters = format!(
                "{}{}",
                line.chars().nth(i).unwrap(),
                line.chars().nth(i + 1).unwrap()
            );
            if letters == "ab" || letters == "cd" || letters == "pq" || letters == "xy" {
                return true;
            }
        }
    }
    false
}

fn vowel(c: char) -> bool {
    "aeiou".contains(c)
}

fn three_vowels(line: &String) -> bool {
    let mut vowel_count = 0;

    for char in line.chars() {
        if vowel(char) {
            vowel_count += 1;
        }
    }

    vowel_count >= 3
}

fn double_letter(line: &String) -> bool {
    for (i, _) in line.chars().enumerate() {
        if i < line.len() - 1 {
            if line.chars().nth(i).unwrap() == line.chars().nth(i + 1).unwrap() {
                return true;
            }
        }
    }
    false
}

fn in_between(line: &String) -> bool {
    for (i, _) in line.chars().enumerate() {
        if i > 0 && i < line.len() - 1 {
            let a = line.chars().nth(i - 1).unwrap();
            let b = line.chars().nth(i + 1).unwrap();
            if a == b {
                return true;
            }
        }
    }
    false
}

struct Pair {
    chars: String,
    index: usize,
}

fn abs(a: i64) -> i64 {
    if a >= 0 {
        return a;
    }
    -a
}

fn PairsContain(pairs: &Vec<Pair>, chars: &String) -> i64 {
    for pair in pairs {
        if pair.chars == *chars {
            return pair.index as i64;
        }
    }
    return -1;
}

fn contains_pair(line: &String) -> bool {
    println!("Analyzing {line}: ");
    let mut pairs: Vec<Pair> = vec![];
    for (i, _) in line.chars().enumerate() {
        if i < line.len() - 1 {
            let a = line.chars().nth(i).unwrap();
            let b = line.chars().nth(i + 1).unwrap();
            let chars = format!("{a}{b}");
            println!("\t\tFound double: {chars}");
            let res = PairsContain(&pairs, &chars);
            if res >= 0 {
                println!("\t\tFound double double: {chars}");
                if abs(res - i as i64) >= 2 {
                    return true;
                }
            } else {
                pairs.push(Pair { chars, index: i });
            }
        }
    }
    false
}
