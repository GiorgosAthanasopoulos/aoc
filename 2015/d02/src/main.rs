use std::{env, fs::File, io::BufReader, io::BufRead};

fn main() -> Result<(), &'static str>{
    let args: Vec<String> = env::args().collect();
    if args.len() < 2 {
        return Err("Missing argument: filename")
    }
    let filename = args[1].clone();
    let file = File::open(filename).unwrap();
    let reader = BufReader::new(file);

    let mut sum = 0;
    for line in reader.lines() {
        let line_copy = line.unwrap().clone();
        let parts = line_copy.split("x");
        let mut l: i64 = 0;
        let mut w: i64 = 0;
        let mut h: i64 = 0;
        for (i, part) in parts.enumerate() {
            if i == 0 {
                l = part.to_string().parse::<i64>().unwrap();
            } else if i == 1 {
                w = part.to_string().parse::<i64>().unwrap();
            } else if i == 2{
                h = part.to_string().parse::<i64>().unwrap();
            }
        }

        /* part one
        let side_a = l*w;
        let side_b = w*h;
        let side_c = h*l;
        sum += 2*side_a + 2*side_b + 2*side_c + min(side_a, side_b, side_c);
        */
        sum += 2*min(l, w, h) + 2*secondMin(l, w, h);
        sum += l * w * h;
    }

    println!("{sum}");
    Ok(())
}

fn min(a: i64, b: i64, c: i64) -> i64 {
    let mut min = a;
    if b < min {
        min = b;
    }
    if c < min {
        min = c;
    }
    min
}

fn secondMin(a: i64, b: i64, c: i64) -> i64 {
    let mut arr = vec![a, b, c];
    arr.sort();
    arr[1]
}
