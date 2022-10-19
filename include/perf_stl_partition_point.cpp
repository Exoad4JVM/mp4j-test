

#include <algorithm>
#include <iostream>
#include <numeric>
#include <vector>

#include "perf.hpp"

int rand_int()
{
    return static_cast<int>((rand() / double(RAND_MAX)) * 25.0);
}

bool less_than_20(int value)
{
    return value < 20;
}

int main(int argc, char *argv[])
{
    perf_parse_args(argc, argv);
    std::cout << "size: " << PERF_N << std::endl;

    // create vector of random numbers on the host
    std::vector<int> host_vector(PERF_N);
    std::generate(host_vector.begin(), host_vector.end(), rand_int);
    std::partition(host_vector.begin(), host_vector.end(),
                    less_than_20);
    perf_timer t;
    for(size_t trial = 0; trial < PERF_TRIALS; trial++){
        t.start();
        std::partition_point(host_vector.begin(), host_vector.end(),
                             less_than_20);
        t.stop();
    }
    std::cout << "time: " << t.min_time() / 1e6 << " ms" << std::endl;

    return 0;
}
